package com.satya.spring.reactive.microservice.sec06;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@AutoConfigureWebTestClient
@SpringBootTest(properties = "sec=sec06")
public class CalculatorTest {

  @Autowired private WebTestClient webTestClient;

  @Test
  public void testCalculator() {
    validate(20, 10, "+", 200, "30");
    validate(20, 10, "-", 200, "10");
    validate(20, 10, "*", 200, "200");
    validate(20, 10, "/", 200, "2");

    // bad headers
    validate(20, 0, "+", 400, "b should not be 0");
    validate(20, 10, "@", 400, "Operation header should be one of +, -, *, /");
    validate(20, 10, null, 400, "Operation header should be one of +, -, *, /");
  }

  private void validate(int a, int b, String operation, int statusCode, String expectedResult) {
    this.webTestClient
        .get()
        .uri("/calculator/{1}/{2}", a, b)
        .headers(
            h -> {
              if (operation != null) {
                h.set("operation", operation);
              }
            })
        .exchange()
        .expectStatus()
        .isEqualTo(statusCode)
        .expectBody(String.class)
        .value(
            value -> {
              Assertions.assertNotNull(value);
              Assertions.assertEquals(value, expectedResult);
            });
  }
}
