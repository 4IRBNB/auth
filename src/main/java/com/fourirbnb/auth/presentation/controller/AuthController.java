package com.fourirbnb.auth.presentation.controller;


import com.fourirbnb.auth.application.service.AuthService;
import com.fourirbnb.auth.presentation.dto.LoginUserRequest;
import com.fourirbnb.auth.presentation.dto.SignUpAuthRequest;
import com.fourirbnb.auth.presentation.dto.SignUpAuthResponse;
import com.fourirbnb.common.exception.InvalidParameterException;
import com.fourirbnb.common.response.BaseResponse;
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
  public BaseResponse<SignUpAuthResponse> registerUser(
      @Valid @RequestBody SignUpAuthRequest request) {
    try {
      authService.signUp(request);
      return BaseResponse.SUCCESS(null, "회원가입 성공", HttpStatus.CREATED.value());
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

}
