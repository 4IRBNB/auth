package com.fourirbnb.auth.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangePasswordRequest {

  @NotBlank(message = "기존 비밀번호를 입력해주세요")
  @Size(min = 8, max = 15, message = "비밀번호는 8~15자로 입력해야 합니다.")
  @Pattern(
      regexp = "^[A-Za-z0-9!@#$%^&*()_+=~]+$",
      message = "비밀번호는 영문자, 숫자, 특수문자(!@#$...)만 사용할 수 있습니다."
  )
  String currentPassword;

  @NotBlank(message = "변경할 비밀번호를 입력해주세요")
  @Size(min = 8, max = 15, message = "비밀번호는 8~15자로 입력해야 합니다.")
  @Pattern(
      regexp = "^[A-Za-z0-9!@#$%^&*()_+=~]+$",
      message = "비밀번호는 영문자, 숫자, 특수문자(!@#$...)만 사용할 수 있습니다."
  )
  String newPassword;

}
