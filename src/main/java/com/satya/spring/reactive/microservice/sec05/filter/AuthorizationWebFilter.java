package com.satya.spring.reactive.microservice.sec05.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Service
@Order(2)
public class AuthorizationWebFilter implements WebFilter {

  public static final Logger log = LoggerFactory.getLogger(AuthorizationWebFilter.class);

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    log.info("AuthorizationWebFilter.filter");
    var category = exchange.getAttributeOrDefault("category", Category.STANDARD);
    return switch (category) {
      case STANDARD -> standard(exchange, chain);
      case PRIME -> chain.filter(exchange);
    };
  }

  private Mono<Void> prime(ServerWebExchange exchange, WebFilterChain chain) {
    log.info("AuthorizationWebFilter.prime");
    return chain.filter(exchange);
  }

  private Mono<Void> standard(ServerWebExchange exchange, WebFilterChain chain) {
    log.info("AuthorizationWebFilter.standard");
    var isGet = HttpMethod.GET.equals(exchange.getRequest().getMethod());
    if (isGet) {
      return chain.filter(exchange);
    }
    return Mono.fromRunnable(() -> exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN));
  }
}
