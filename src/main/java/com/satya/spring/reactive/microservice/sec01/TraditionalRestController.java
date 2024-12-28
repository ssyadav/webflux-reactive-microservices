package com.satya.spring.reactive.microservice.sec01;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("traditional")
public class TraditionalRestController {

  public static final Logger log = LoggerFactory.getLogger(TraditionalRestController.class);

  private RestClient restClient =
      RestClient.builder()
          .requestFactory(new JdkClientHttpRequestFactory())
          .baseUrl("http://localhost:7070")
          .build();

  @GetMapping("products")
  public List<Product> products() {
    log.info("getting products");
    var products =
        restClient
            .get()
            .uri("/demo01/products")
            .retrieve()
            .body(new ParameterizedTypeReference<List<Product>>() {});
    log.info("products: {}", products);
    return products;
  }

  @GetMapping("products/mistake")
  public Flux<Product> productsMistake() {
    log.info("getting products");
    var products =
        restClient
            .get()
            .uri("/demo01/products")
            .retrieve()
            .body(new ParameterizedTypeReference<List<Product>>() {});
    log.info("products: {}", products);
    return Flux.fromIterable(products);
  }

  @GetMapping("products/notorious")
  public List<Product> productsNotorious() {
    log.info("getting products");
    var products =
            restClient
                    .get()
                    .uri("/demo01/products/notorious")
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<Product>>() {});
    log.info("products: {}", products);
    return products;
  }
}
