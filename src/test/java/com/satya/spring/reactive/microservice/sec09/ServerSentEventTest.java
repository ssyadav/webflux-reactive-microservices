package com.satya.spring.reactive.microservice.sec09;

import com.satya.spring.reactive.microservice.sec09.dto.ProductDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

@AutoConfigureWebTestClient
@SpringBootTest(properties = {"sec=sec09"})
public class ServerSentEventTest {

  public static final Logger log = LoggerFactory.getLogger(ServerSentEventTest.class);

  @Autowired private WebTestClient client;

  @Test
  public void testServerSentEvent() {
    this.client
        .get()
        .uri("/products/stream/80")
        .accept(MediaType.TEXT_EVENT_STREAM)
        .exchange()
        .expectStatus()
        .isOk()
        .expectHeader()
        .contentType("text/event-stream;charset=UTF-8")
        .returnResult(ProductDto.class)
        .getResponseBody()
        .take(3)
        .doOnNext(dto -> log.info("received: {}", dto))
        .collectList()
        .as(StepVerifier::create)
        .assertNext(
            dto -> {
              Assertions.assertEquals(3, dto.size());
              Assertions.assertTrue(dto.stream().allMatch(p -> p.price() <= 80));
            })
        .expectComplete()
        .verify();
  }
}
