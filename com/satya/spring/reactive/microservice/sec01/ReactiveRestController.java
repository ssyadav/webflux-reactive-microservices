package com.satya.spring.reactive.microservice.sec01;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("reactive")
public class ReactiveRestController {

  public static final Logger log = LoggerFactory.getLogger(ReactiveRestController.class);

  private final WebClient webClient = WebClient.builder()
          .baseUrl("http://localhost:7070")
          .build();

  @GetMapping("products")
  public Flux<Product> products() {
    log.info("getting products");
    var products = webClient.get().uri("/demo01/products")
            .retrieve()
            .bodyToFlux(Product.class)
            .doOnNext(product -> log.info("product: {}", product));
    return products;
  }

    @GetMapping(value = "products/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Product> productsWithStream() {
        log.info("getting products");
        var products = webClient.get().uri("/demo01/products")
                .retrieve()
                .bodyToFlux(Product.class)
                .doOnNext(product -> log.info("product: {}", product));
        return products;
    }

    @GetMapping("products/notorious")
    public Flux<Product> productsNotorious() {
        log.info("getting products");
        var products = webClient.get().uri("/demo01/products/notorious")
                .retrieve()
                .bodyToFlux(Product.class)
                .onErrorComplete()
                .doOnNext(product -> log.info("product: {}", product));
        return products;
    }
}