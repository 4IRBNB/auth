package com.fourirbnb.auth.presentation.dto;

import com.fourirbnb.auth.domain.model.ApprovalStatus;
import com.fourirbnb.auth.domain.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserInternalRequest {

  private String email;
  private String password;
  private String nickname;
  private String username;
  private String phone;
  private String slackId;
  private Role role;
  private ApprovalStatus status;
}
