package com.fourirbnb.auth.presentation.dto;

import com.fourirbnb.auth.domain.model.Role;
import lombok.Getter;

@Getter
public class LoginUserRequest {

  String email;
  String password;
  Role role;
}
