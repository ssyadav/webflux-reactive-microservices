package com.satya.spring.reactive.microservice.sec07;


import com.satya.spring.reactive.microservice.sec07.dto.CalculatorResponse;
import com.satya.spring.reactive.microservice.sec07.dto.Product;
import org.junit.jupiter.api.Test;
import org.springframework.http.ProblemDetail;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Map;

public class Lec06QueryParamTest extends AbstractWebClient {


    private WebClient webClient = createWebClient();


    @Test
    public void testUriBuilderVariable() {
        var path = "/lec06/calculator";
        var query = "first={first}&second={second}&operation={operation}";
        this.webClient
                .get()
                .uri(uriBuilder -> uriBuilder.path(path).query(query).build(10, 20, "+"))
                .retrieve()
                .bodyToMono(CalculatorResponse.class)
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    @Test
    public void testUriBuilderMap() {
        var path = "/lec06/calculator";
        var query = "first={first}&second={second}&operation={operation}";
//        Map<String, Object> map = Map.of("first", 10, "second", 200, "operation", "+");
        var map = Map.of("first", 10, "second", 200, "operation", "+");
        this.webClient
                .get()
                .uri(uriBuilder -> uriBuilder.path(path).query(query).build(map))
                .retrieve()
                .bodyToMono(CalculatorResponse.class)
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

}
