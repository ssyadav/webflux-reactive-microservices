package com.satya.spring.reactive.microservice.sec07;


import com.satya.spring.reactive.microservice.sec07.dto.CalculatorResponse;
import java.util.Map;

import com.satya.spring.reactive.microservice.sec07.dto.Product;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

public class Lec07BasicAuthTest extends AbstractWebClient {


    private WebClient webClient = createWebClient(b -> b.defaultHeaders(h -> h.setBasicAuth("java", "secret")));


    @Test
    public void testBasicAuth() {
        this.webClient
                .get()
                .uri("/lec07/product/{id}", 1)
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

}
