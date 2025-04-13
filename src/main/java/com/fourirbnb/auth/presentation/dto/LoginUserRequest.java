package com.fourirbnb.auth.presentation.dto;

import lombok.Getter;

@Getter
public class LoginUserRequest {

  String email;
  String password;
}
