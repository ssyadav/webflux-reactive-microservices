package com.satya.spring.reactive.microservice.sec02;

import com.satya.spring.reactive.microservice.sec02.repository.CustomerOrderRepository;
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

public class Lec01CustomerOrderRepositoryTest extends AbstractTest {
  public static final Logger log = LoggerFactory.getLogger(Lec01CustomerOrderRepositoryTest.class);

  @Autowired private CustomerOrderRepository customerOrderRepository;

  @Test
  public void getProductOrderedByCustomer() {
    this.customerOrderRepository
        .getProductOrderedByCustomer("sam")
        .doOnNext(c -> log.info("customer: {}", c))
        .as(StepVerifier::create)
        .expectNextCount(2)
        .expectComplete()
        .verify();
  }

  @Test
  public void getOrdersDetailsByDescription() {
    this.customerOrderRepository
            .getOrdersDetailsByDescription("iphone 20")
            .doOnNext(c -> log.info("customer: {}", c))
            .as(StepVerifier::create)
            .assertNext(c -> Assertions.assertEquals(975, c.amount()))
            .assertNext(c -> Assertions.assertEquals(950, c.amount()))
            .expectComplete()
            .verify();
  }


}
