package com.fourirbnb.auth.presentation.controller;

import com.fourirbnb.auth.application.service.AuthService;
import com.fourirbnb.auth.domain.model.Role;
import com.fourirbnb.auth.presentation.dto.SignUpUserRequest;
import com.fourirbnb.common.exception.InvalidParameterException;
import com.fourirbnb.common.response.BaseResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/admin")
@AllArgsConstructor
public class AuthInternalController {

  private AuthService authService;

  //MASTER
  @PostMapping("/adminSignUp")
  public BaseResponse<?> adminSignUp(@RequestBody SignUpUserRequest request) {
    try {
      authService.signUp(request, Role.MASTER);
      return BaseResponse.SUCCESS(null, "관리자 회원가입 성공", HttpStatus.CREATED.value());
    } catch (InvalidParameterException e) {
      return BaseResponse.FAIL(e.getMessage(), HttpStatus.BAD_REQUEST.value());
    }
  }

  //MANAGER
  @PostMapping("/managerSignUp")
  public BaseResponse<?> managerSignUp(@RequestBody SignUpUserRequest request) {

    try {
      authService.signUp(request, Role.MANAGER);
      return BaseResponse.SUCCESS(null, "매니저 회원가입 성공 승인대기중", HttpStatus.CREATED.value());
    } catch (InvalidParameterException e) {
      return BaseResponse.FAIL(e.getMessage(), HttpStatus.BAD_REQUEST.value());
    }
  }

}
