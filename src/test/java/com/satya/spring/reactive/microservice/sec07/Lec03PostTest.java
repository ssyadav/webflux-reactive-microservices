package com.satya.spring.reactive.microservice.sec07;


import com.satya.spring.reactive.microservice.sec07.dto.Product;
import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class Lec03PostTest extends AbstractWebClient {


    private WebClient webClient = createWebClient();


    @Test
    public void testPostBodyValue() {
        var product = new Product(null, "Product", 100);
        this.webClient
                .post()
                .uri("/lec03/product")
                .bodyValue(product)
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    @Test
    public void testPostBody() {
//        var product = Mono.just(new Product(null, "Product", 100));
        var product = Mono.fromSupplier(() -> new Product(null, "Product", 1000))
                .delayElement(Duration.ofSeconds(1));
        this.webClient
                .post()
                .uri("/lec03/product")
                .body(product, Product.class)
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }


}
