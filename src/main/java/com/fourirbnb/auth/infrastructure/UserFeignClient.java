package com.fourirbnb.auth.infrastructure;

import com.fourirbnb.auth.presentation.dto.CreateUserInternalRequest;
import com.fourirbnb.auth.presentation.dto.LoginUserResponse;
import com.fourirbnb.auth.presentation.dto.UserInternalResponse;
import com.fourirbnb.common.FeignInterceptor.NoAuthFeignClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user")
@NoAuthFeignClient
public interface UserFeignClient {

  @PostMapping("/internal/users/signUp")
  ResponseEntity<Void> userSignUp(@RequestBody CreateUserInternalRequest request);

  @GetMapping("/internal/users/email/{email}")
  ResponseEntity<LoginUserResponse> findByEmail(@PathVariable String email);

  @GetMapping("/internal/getPassword")
  ResponseEntity<UserInternalResponse> getEncryptedPassword(Long id);

  @GetMapping("/internal/updatePassword")
  ResponseEntity<Void> updatePassword(Long id,
      String password);
}
