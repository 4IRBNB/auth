package com.fourirbnb.auth.application.mapper;

import com.fourirbnb.auth.presentation.dto.request.CreateUserFeignRequest;
import com.fourirbnb.auth.presentation.dto.request.SignUpUserRequest;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = false))
public interface AuthDtoMapper {

  default CreateUserFeignRequest toCreateInternalDto(SignUpUserRequest request) {
    return CreateUserFeignRequest.builder()
        .email(request.getEmail())
        .nickname(request.getNickname())
        .username(request.getUsername())
        .phone(request.getPhone())
        .slackId(request.getSlackId())
        .role("CUSTOMER")
        .build();
  }
}
