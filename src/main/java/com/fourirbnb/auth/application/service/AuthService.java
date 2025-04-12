package com.fourirbnb.auth.application.service;


import com.fourirbnb.auth.application.mapper.AuthDtoMapper;
import com.fourirbnb.auth.application.mapper.AuthMapper;
import com.fourirbnb.auth.domain.entity.Auth;
import com.fourirbnb.auth.domain.repository.AuthRepository;
import com.fourirbnb.auth.infrastructure.UserFeignClient;
import com.fourirbnb.auth.presentation.dto.request.CreateUserFeignRequest;
import com.fourirbnb.auth.presentation.dto.request.SignUpUserRequest;
import com.fourirbnb.common.exception.InvalidParameterException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

  private final AuthDtoMapper authDtoMapper;
  private final AuthMapper authMapper;
  private final UserFeignClient userFeignClient;
  private final PasswordEncoder passwordEncoder;
  private final AuthRepository authRepository;

  //차후 kafka 도입 고려 설계
  public void signUp(SignUpUserRequest request) {
    String hashedPassword = passwordEncoder.encode(request.getPassword());

    CreateUserFeignRequest feignRequest = authDtoMapper.toCreateInternalDto(request);

    boolean result = userFeignClient.userSignUp(feignRequest);

    if (!result) {
      throw new InvalidParameterException("이메일이 이미 존재합니다.");
    }

    Auth auth = authMapper.toEntity(request.getEmail(), hashedPassword);
    authRepository.save(auth);
  }
}
