package com.fourirbnb.auth.application.service;


import com.fourirbnb.auth.application.mapper.AuthDtoMapper;
import com.fourirbnb.auth.domain.model.Role;
import com.fourirbnb.auth.infrastructure.UserFeignClient;
import com.fourirbnb.auth.presentation.dto.ChangePasswordRequest;
import com.fourirbnb.auth.presentation.dto.CreateUserInternalRequest;
import com.fourirbnb.auth.presentation.dto.LoginUserRequest;
import com.fourirbnb.auth.presentation.dto.LoginUserResponse;
import com.fourirbnb.auth.presentation.dto.SignUpUserRequest;
import com.fourirbnb.auth.presentation.dto.UpdatePasswordRequest;
import com.fourirbnb.auth.presentation.dto.UserInternalResponse;
import com.fourirbnb.common.exception.InternalServerException;
import com.fourirbnb.common.exception.InvalidParameterException;
import com.fourirbnb.common.exception.ResourceNotFoundException;
import feign.FeignException;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

  private final UserFeignClient userFeignClient;
  private final PasswordEncoder passwordEncoder;


  @Value("${spring.application.name}")
  private String issuer;

  @Value("${jwt.access-expiration}")
  private Long accessExpiration;

  private final SecretKey secretKey;

  /*
   권한(Role)에 따라 서비스 로직을 분리하는 것이 구조적으로는 맞지만,
   구현 복잡도와 프로젝트 리소스를 고려해
   하나의 메서드 내에서 역할별 분기 처리로 간소화하여 진행하였습니다.

   추후 확장 시 각 역할(Role)에 따른 서비스/로직 분리를 고려할 수 있습니다.
 */
  public void signUp(SignUpUserRequest request, Role role) {

    String hashedPassword = passwordEncoder.encode(request.getPassword());

    CreateUserInternalRequest feignRequest = AuthDtoMapper.toCreateInternalDto(request,
        hashedPassword, role);

    //상태코드로 분기구분
    try {
      // 조건문으로 CUSTOMER MASTER 일때 그냥 바로 저장 HOST MANAGER 일때 Staging 이동
      if (role == Role.CUSTOMER) {
        userFeignClient.userSignUp(feignRequest);
      } else if (role == Role.HOST || role == Role.MANAGER) {
        userFeignClient.saveStagingRequest(feignRequest);
      } else if (role == Role.MASTER) {
        userFeignClient.adminSignUp(feignRequest);
      } else {
        throw new InvalidParameterException("잘못된 권한입니다.");
      }
    } catch (FeignException e) {
      if (e.status() == 400) {
        throw new InvalidParameterException(e.contentUTF8());
      } else {
        throw new InternalServerException(e.getMessage());
      }
    } catch (Exception e) {
      throw new InternalServerException("회원가입 처리 중 서버 오류가 발생했습니다.");
    }
  }

  /*
      빠른 구현을 위해서 Role 은 프론트에서 받는거로 처리
      실제로는 메서드를 분리하고 컨트롤러를 나눠야하지만 시간관계상 해당 방식으로 진행하였습니다.
   */
  public String signIn(LoginUserRequest request) {
    ResponseEntity<LoginUserResponse> response;

    if (request.getRole() == Role.CUSTOMER || request.getRole() == Role.HOST) {
      response = userFeignClient.findByEmail(request.getEmail());
    } else if (request.getRole() == Role.MASTER || request.getRole() == Role.MANAGER) {
      response = userFeignClient.findAdminByEmail(request.getEmail());
    } else {
      throw new InvalidParameterException("잘못된 권한입니다.");
    }

    LoginUserResponse signInDto = response.getBody();
    if (signInDto == null || signInDto.email() == null) {
      throw new ResourceNotFoundException("회원을 찾을수 없습니다..");
    } else if (signInDto.password() == null) {
      throw new ResourceNotFoundException("아이디 또는 비밀번호가 일치하지 않습니다.");
    }
    if (!passwordEncoder.matches(request.getPassword(), signInDto.password())) {
      throw new InvalidParameterException("아이디 또는 비밀번호가 일치하지 않습니다.");
    }

    return createAccessToken(signInDto);
  }

  //  만료시간 refresh 토큰 도입 이후 짧게 테스트
  private String createAccessToken(LoginUserResponse signInDto) {
    return Jwts.builder()
        .claim("id", String.valueOf(signInDto.id()))
        .claim("role", signInDto.role().name())
        .issuer(issuer)
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + accessExpiration))
        .signWith(secretKey, Jwts.SIG.HS512)
        .compact();
  }

  public void changePassword(Long id, ChangePasswordRequest request) {
    String currentPassword = request.getCurrentPassword();
    String newPassword = request.getNewPassword();

    ResponseEntity<UserInternalResponse> response = userFeignClient.getEncryptedPassword(id);
    UserInternalResponse body = response.getBody();

    if (currentPassword == null || newPassword == null) {
      throw new InvalidParameterException("비밀번호를 입력해주세요");
    }
    if (body == null) {
      throw new InvalidParameterException("회원 정보가 없습니다");
    }

    //기존
    String password = body.password();

    if (!passwordEncoder.matches(currentPassword, password)) {
      throw new InvalidParameterException("기존 비밀번호가 일치하지 않습니다.");
    }

    String encodeNewPassword = passwordEncoder.encode(newPassword);
    userFeignClient.updatePassword(new UpdatePasswordRequest(id, encodeNewPassword));
  }

}
