package com.satya.spring.reactive.microservice.sec02;

import com.satya.spring.reactive.microservice.sec02.entity.Customer;
import com.satya.spring.reactive.microservice.sec02.repository.CustomerRepository;
import com.satya.spring.reactive.microservice.sec02.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import reactor.test.StepVerifier;

public class Lec01ProductrRepositoryTest extends AbstractTest {
  public static final Logger log = LoggerFactory.getLogger(Lec01ProductrRepositoryTest.class);

  @Autowired private ProductRepository productRepository;

  @Test
  public void findByPriceBetween() {
    this.productRepository
        .findByPriceBetween(750, 1000)
        .doOnNext(c -> log.info("customer: {}", c))
        .as(StepVerifier::create)
        .expectNextCount(3)
        .expectComplete()
        .verify();
  }

  @Test
  public void findByTestPageable() {
    Pageable pageable = PageRequest.of(0, 3).withSort(Sort.by("price").descending());
    this.productRepository
            .findBy(pageable)
            .doOnNext(c -> log.info("customer: {}", c))
            .as(StepVerifier::create)
            .expectNextCount(3)
            .expectComplete()
            .verify();
  }


}
