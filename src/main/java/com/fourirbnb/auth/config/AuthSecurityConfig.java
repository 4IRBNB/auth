package com.fourirbnb.auth.config;

import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AuthSecurityConfig {

  @Value("${jwt.secret}")
  private String secret;

//  @Bean
//  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//    http
//        // CSRF 보호 비활성화
//        .csrf(AbstractHttpConfigurer::disable)
//        .authorizeHttpRequests(authorize -> authorize // 요청에 대한 접근 권한을 설정합니다.
//            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//            .requestMatchers(
//                "/api/auth/signUp",
//                "/h2-console/**"
//
//            ).permitAll()
//            // 그 외의 모든 요청은 인증이 필요합니다.
//            .anyRequest().authenticated()
//        )
//        .anonymous(Customizer.withDefaults())
//        //STATELESS로 설정
//        .sessionManagement(session -> session
//            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//        )
//        .headers(headers -> headers
//            .frameOptions(FrameOptionsConfig::sameOrigin)
//        );
//    return http.build();
//  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecretKey jwtSecretKey() {
    return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
  }

}
