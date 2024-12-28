package com.satya.spring.reactive.microservice.sec08;

import com.satya.spring.reactive.microservice.sec08.dto.ProductDto;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.nio.file.Path;
import java.time.Duration;

/*
    Just for demo
 */
public class ProductsUploadDownloadTest {

    private static final Logger log = LoggerFactory.getLogger(ProductsUploadDownloadTest.class);
    private final ProductClient productClient = new ProductClient();

    @Test
    public void uploadOne() {
        var flux = Flux.just(new ProductDto(null, "product-", 1000))
                .delayElements(Duration.ofSeconds(10));

        this.productClient.uploadProducts(flux)
                .doOnNext(r -> log.info("received: {}", r))
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    @Test
    public void uploadTen() {
        var flux = Flux.range(1, 10)
                .map(i -> new ProductDto(null, "product-" + i, i))
                .delayElements(Duration.ofSeconds(2));

        this.productClient.uploadProducts(flux)
                .doOnNext(r -> log.info("received: {}", r))
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    @Test
    public void uploadMillion() {
        var flux = Flux.range(1, 1000000)
                .map(i -> new ProductDto(null, "product-" + i, i));

        this.productClient.uploadProducts(flux)
                .doOnNext(r -> log.info("received: {}", r))
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }


    @Test
    public void download() {
        this.productClient.downloadProducts()
                .map(ProductDto::toString)
                .as(flux -> FileWriter.create(flux, Path.of("products.txt")))
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

}