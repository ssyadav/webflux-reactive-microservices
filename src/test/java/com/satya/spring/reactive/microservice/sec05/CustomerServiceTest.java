package com.satya.spring.reactive.microservice.sec05;

import com.satya.spring.reactive.microservice.sec05.dto.CustomerDto;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

@AutoConfigureWebTestClient
@SpringBootTest(properties = {"sec=sec05"})
public class CustomerServiceTest {

  public static final Logger log = LoggerFactory.getLogger(CustomerServiceTest.class);

  @Autowired private WebTestClient client;

  @Test
  public void testUnauthorized() {
    log.info("CustomerServiceTest.testUnauthorized: ");

    // no token
    this.client.get()
            .uri("/customers")
            .exchange()
            .expectStatus()
            .isEqualTo(HttpStatus.UNAUTHORIZED);

    // invalid token
    this.validateGet("secret", HttpStatus.UNAUTHORIZED);
  }

  @Test
  public void testStandardCategory() {
    this.validateGet("secret123", HttpStatus.OK);
    this.validatePost("secret123", HttpStatus.FORBIDDEN);
  }

  @Test
  public void testPrimeCategory() {
    this.validateGet("secret456", HttpStatus.OK);
    this.validatePost("secret456", HttpStatus.OK);
  }

  private void validateGet(String token, HttpStatus expectedStatus) {
    this.client.get()
            .uri("/customers")
            .header("auth-token", token)
            .exchange()
            .expectStatus()
            .isEqualTo(expectedStatus);
  }

  private void validatePost(String token, HttpStatus expectedStatus) {
    var customerDto = new CustomerDto(null, "satya", "satya@gmail.com");
    this.client.post()
            .uri("/customers")
            .header("auth-token", token)
            .bodyValue(customerDto)
            .exchange()
            .expectStatus()
            .isEqualTo(expectedStatus);
  }

}
