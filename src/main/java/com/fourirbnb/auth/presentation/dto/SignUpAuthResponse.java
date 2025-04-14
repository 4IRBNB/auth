package com.fourirbnb.auth.presentation.dto;

import com.fourirbnb.auth.domain.model.Role;

public record SignUpAuthResponse(
    String email,
    String password,
    String nickname,
    String username,
    String phone,
    String slackId,
    Role role
) {

}
