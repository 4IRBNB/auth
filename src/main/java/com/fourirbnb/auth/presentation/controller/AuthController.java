package com.fourirbnb.auth.presentation.controller;


import com.fourirbnb.auth.application.service.AuthService;
import com.fourirbnb.auth.domain.model.Role;
import com.fourirbnb.auth.presentation.dto.ChangePasswordRequest;
import com.fourirbnb.auth.presentation.dto.LoginUserRequest;
import com.fourirbnb.auth.presentation.dto.SignUpUserRequest;
import com.fourirbnb.auth.presentation.dto.SignUpUserResponse;
import com.fourirbnb.common.exception.InvalidParameterException;
import com.fourirbnb.common.response.BaseResponse;
import com.fourirbnb.common.security.AuthenticatedUser;
import com.fourirbnb.common.security.UserInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

  private final AuthService authService;

  @PostMapping("/signUp")
  public BaseResponse<SignUpUserResponse> userSignUp(
      @Valid @RequestBody SignUpUserRequest request) {
    try {
      authService.signUp(request, Role.CUSTOMER);
      return BaseResponse.SUCCESS(null, "유저 회원가입 성공", HttpStatus.CREATED.value());
    } catch (InvalidParameterException e) {
      return BaseResponse.FAIL(e.getMessage(), HttpStatus.BAD_REQUEST.value());
    }
  }

  @PostMapping("/hostSignUp")
  public BaseResponse<SignUpUserResponse> hostSignUp(
      @Valid @RequestBody SignUpUserRequest request) {
    try {
      authService.signUp(request, Role.HOST);
      return BaseResponse.SUCCESS(null, "호스트 회원가입 성공 승인대기중", HttpStatus.CREATED.value());
    } catch (InvalidParameterException e) {
      return BaseResponse.FAIL(e.getMessage(), HttpStatus.BAD_REQUEST.value());
    }
  }

  @PostMapping("/signIn")
  public BaseResponse<String> signIn(@RequestBody LoginUserRequest request) {
    String token = authService.signIn(request);
    if (token == null) {
      return BaseResponse.FAIL("아이디와 비밀번호를 확인해주세요", HttpStatus.UNAUTHORIZED.value());
    }
    return BaseResponse.SUCCESS(token, "로그인 성공");
  }

  @PostMapping("/changePassword")
  public BaseResponse<Void> changePassword(
      @AuthenticatedUser UserInfo user, @RequestBody ChangePasswordRequest request) {
    Long id = user.getUserId();
    authService.changePassword(id, request);
    return BaseResponse.SUCCESS(null, "비밀번호 변경이 성공하였습니다.", HttpStatus.OK.value());
  }

}
