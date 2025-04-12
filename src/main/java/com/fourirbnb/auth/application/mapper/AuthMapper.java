package com.fourirbnb.auth.application.mapper;

import com.fourirbnb.auth.domain.entity.Auth;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthMapper {

  default Auth toEntity(String email, String hashedPassword) {
    return Auth.of(email, hashedPassword);
  }
}