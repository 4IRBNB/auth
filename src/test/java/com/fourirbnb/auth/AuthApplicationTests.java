package com.fourirbnb.auth;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "common.jpa.enabled=false")
class AuthApplicationTests {

  @Test
  void contextLoads() {
  }

}
