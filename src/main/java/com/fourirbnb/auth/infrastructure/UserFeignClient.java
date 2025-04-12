package com.fourirbnb.auth.infrastructure;

import com.fourirbnb.auth.presentation.dto.request.CreateUserFeignRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user")
public interface UserFeignClient {

  @PostMapping("/internal/users/signUp")
  boolean userSignUp(@RequestBody CreateUserFeignRequest request);
}
