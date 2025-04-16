package com.fourirbnb.auth.application.service;


import com.fourirbnb.auth.application.mapper.AuthDtoMapper;
import com.fourirbnb.auth.infrastructure.UserFeignClient;
import com.fourirbnb.auth.presentation.dto.ChangePasswordRequest;
import com.fourirbnb.auth.presentation.dto.CreateUserInternalRequest;
import com.fourirbnb.auth.presentation.dto.LoginUserRequest;
import com.fourirbnb.auth.presentation.dto.LoginUserResponse;
import com.fourirbnb.auth.presentation.dto.SignUpAuthRequest;
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


  public void signUp(SignUpAuthRequest request) {
    String hashedPassword = passwordEncoder.encode(request.getPassword());

    CreateUserInternalRequest feignRequest = AuthDtoMapper.toCreateInternalDto(request,
        hashedPassword);
    //상태코드로 분기구분
    try {
      userFeignClient.userSignUp(feignRequest);
    } catch (FeignException e) {
      if (e.status() == 400) {
        throw new InvalidParameterException(e.contentUTF8());
      }
    } catch (Exception e) {
      throw new InternalServerException("회원가입 처리 중 서버 오류가 발생했습니다.");
    }
  }

  public String signIn(LoginUserRequest request) {
    ResponseEntity<LoginUserResponse> response = userFeignClient.findByEmail(request.getEmail());
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
    userFeignClient.updatePassword(id, encodeNewPassword);
  }

  
}
