package com.fourirbnb.auth.presentation.dto;

import com.fourirbnb.auth.domain.model.Role;

public record LoginUserResponse(
    Long id,
    String email,
    String password,
    String username,
    String nickname,
    String phone,
    String slackId,
    Role role
) {

}
