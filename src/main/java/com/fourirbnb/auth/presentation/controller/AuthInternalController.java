package com.fourirbnb.auth.presentation.controller;

import com.fourirbnb.auth.application.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@AllArgsConstructor
public class AuthInternalController {

  private AuthService authService;

}
