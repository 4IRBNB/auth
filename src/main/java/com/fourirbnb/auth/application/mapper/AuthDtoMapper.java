package com.fourirbnb.auth.application.mapper;

import com.fourirbnb.auth.domain.model.Role;
import com.fourirbnb.auth.presentation.dto.CreateUserInternalRequest;
import com.fourirbnb.auth.presentation.dto.SignUpAuthRequest;

public class AuthDtoMapper {

  public static CreateUserInternalRequest toCreateInternalDto(SignUpAuthRequest request,
      String encodedPassword) {
    return new CreateUserInternalRequest(
        request.getEmail(),
        encodedPassword,
        request.getNickname(),
        request.getUsername(),
        request.getPhone(),
        request.getSlackId(),
        Role.CUSTOMER
    );
  }
}
