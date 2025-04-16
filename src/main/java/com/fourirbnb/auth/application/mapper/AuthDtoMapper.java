package com.fourirbnb.auth.application.mapper;

import com.fourirbnb.auth.domain.model.ApprovalStatus;
import com.fourirbnb.auth.domain.model.Role;
import com.fourirbnb.auth.presentation.dto.CreateUserInternalRequest;
import com.fourirbnb.auth.presentation.dto.SignUpUserRequest;

public class AuthDtoMapper {

  public static CreateUserInternalRequest toCreateInternalDto(SignUpUserRequest request,
      String encodedPassword, Role role) {
    return new CreateUserInternalRequest(
        request.getEmail(),
        encodedPassword,
        request.getNickname(),
        request.getUsername(),
        request.getPhone(),
        request.getSlackId(),
        role,
        ApprovalStatus.APPROVED

    );
  }
  
}
