package com.fourirbnb.auth.presentation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// user-service에 보낼 전용 DTO
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserFeignRequest {

  private String email;
  private String nickname;
  private String username;
  private String phone;
  private String slackId;
  private String role;
}
