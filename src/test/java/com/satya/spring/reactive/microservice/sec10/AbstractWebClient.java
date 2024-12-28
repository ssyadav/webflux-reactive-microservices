package com.satya.spring.reactive.microservice.sec10;

import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

public abstract class AbstractWebClient {

  public static final Logger log = LoggerFactory.getLogger(AbstractWebClient.class);

  protected <T> Consumer<T> print() {
    return item -> log.info("received: {}", item);
  }

  protected WebClient createWebClient() {
    return createWebClient(b -> {});
  }

  protected WebClient createWebClient(Consumer<WebClient.Builder> consumer) {
    var builder = WebClient.builder().baseUrl("http://localhost:7070/demo03");
    consumer.accept(builder);
    return builder.build();
  }
}
