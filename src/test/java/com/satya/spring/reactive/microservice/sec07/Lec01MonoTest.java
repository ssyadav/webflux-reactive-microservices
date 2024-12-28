package com.satya.spring.reactive.microservice.sec07;


import com.satya.spring.reactive.microservice.sec07.dto.Product;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

public class Lec01MonoTest extends AbstractWebClient {


    private WebClient webClient = createWebClient();


    @Test
    public void testSimpleGet() throws InterruptedException {
        this.webClient
                .get()
                .uri("/lec01/product/1")
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(print())
                .subscribe();
        Thread.sleep(Duration.ofSeconds(2));
    }

    @Test
    public void testConcurrentGet() throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            this.webClient
                    .get()
                    .uri("/lec01/product/{id}", i)
                    .retrieve()
                    .bodyToMono(Product.class)
                    .doOnNext(print())
                    .subscribe();
        }

        Thread.sleep(Duration.ofSeconds(2));
    }
}
