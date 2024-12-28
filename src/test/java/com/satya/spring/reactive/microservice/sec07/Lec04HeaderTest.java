package com.satya.spring.reactive.microservice.sec07;


import com.satya.spring.reactive.microservice.sec07.dto.Product;
import java.time.Duration;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class Lec04HeaderTest extends AbstractWebClient {


    private WebClient webClient = createWebClient(b -> b.defaultHeader("caller-id", "order-service"));


    @Test
    public void testDefaultHeader() {
    this.webClient
        .get()
        .uri("/lec04/product/{id}", 1)
        .retrieve()
        .bodyToMono(Product.class)
        .doOnNext(print())
        .then()
        .as(StepVerifier::create)
        .expectComplete()
        .verify();
    }

    @Test
    public void testOverrideHeader() {
        this.webClient
                .get()
                .uri("/lec04/product/{id}", 1)
                .header("caller-id", "payment-service")
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    @Test
    public void testHeaderWithMap() {
        Map<String, String> headers = Map.of("caller-id", "payment11-service", "auth", "Basic 1234");
        this.webClient
                .get()
                .uri("/lec04/product/{id}", 1)
                .headers(h -> h.setAll(headers))
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }


}
