package com.fourirbnb.auth.presentation.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignUpUserRequest {

  @NotBlank(message = "아이디 필수")
  @Email(message = "이메일 형식으로 입력해주세요.")
  private String email;

  @NotBlank(message = "비밀번호 필수")
  @Size(min = 8, max = 15, message = "비밀번호는 8~15자로 입력해야 합니다.")
  @Pattern(
      regexp = "^[A-Za-z0-9!@#$%^&*()_+=~]+$",
      message = "비밀번호는 영문자, 숫자, 특수문자(!@#$...)만 사용할 수 있습니다."
  )
  private String password;

  @NotBlank(message = "닉네임 필수")
  private String nickname;

  @NotBlank(message = "사용자 이름 필수")
  private String username;

  @Email(message = "이메일 형식으로 입력해주세요.")
  private String slackId;

  @Pattern(regexp = "^01[0-9]-\\d{3,4}-\\d{4}$", message = "전화번호 형식이 올바르지 않습니다.")
  private String phone;
}
