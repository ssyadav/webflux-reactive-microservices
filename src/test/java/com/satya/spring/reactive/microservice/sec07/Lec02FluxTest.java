package com.satya.spring.reactive.microservice.sec07;


import com.satya.spring.reactive.microservice.sec07.dto.Product;
import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

public class Lec02FluxTest extends AbstractWebClient {


    private WebClient webClient = createWebClient();


    @Test
    public void testStreamingGetResponse() {
        this.webClient
                .get()
                .uri("/lec02/product/stream")
                .retrieve()
                .bodyToFlux(Product.class)
                .take(Duration.ofSeconds(3))
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }


}
