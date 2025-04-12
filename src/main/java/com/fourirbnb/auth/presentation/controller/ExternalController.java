package com.fourirbnb.auth.presentation.controller;

import com.fourirbnb.auth.application.service.AuthService;
import com.fourirbnb.auth.presentation.dto.request.SignUpUserRequest;
import com.fourirbnb.auth.presentation.dto.response.AuthResponse;
import com.fourirbnb.common.exception.InvalidParameterException;
import com.fourirbnb.common.response.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class ExternalController {

  private final AuthService authService;

  @PostMapping("/signUp")
  public BaseResponse<AuthResponse> registerUser(@Valid @RequestBody SignUpUserRequest request) {
    try {
      authService.signUp(request);
      return BaseResponse.SUCCESS(null, "회원가입 성공", HttpStatus.CREATED.value());
    } catch (InvalidParameterException e) {
      return BaseResponse.FAIL(e.getMessage(), HttpStatus.BAD_REQUEST.value());
    }
  }

}
