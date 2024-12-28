package com.satya.spring.reactive.microservice.sec02;

import com.satya.spring.reactive.microservice.sec02.entity.Customer;
import com.satya.spring.reactive.microservice.sec02.repository.CustomerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

public class Lec01CustomerRepositoryTest extends AbstractTest {
  public static final Logger log = LoggerFactory.getLogger(Lec01CustomerRepositoryTest.class);

  @Autowired private CustomerRepository customerRepository;

  @Test
  public void findAll() {
    this.customerRepository
        .findAll()
        .doOnNext(c -> log.info("customer: {}", c))
        .as(StepVerifier::create)
        .expectNextCount(10)
        .expectComplete()
        .verify();
  }

  @Test
  public void findById() {
    this.customerRepository
        .findById(2)
        .doOnNext(c -> log.info("customer: {}", c))
        .as(StepVerifier::create)
        .assertNext(c -> Assertions.assertEquals("mike", c.getName()))
        .expectComplete()
        .verify();
  }

  @Test
  public void findByName() {
    this.customerRepository
        .findByName("mike")
        .doOnNext(c -> log.info("customer: {}", c))
        .as(StepVerifier::create)
        .assertNext(c -> Assertions.assertEquals("mike", c.getName()))
        .expectComplete()
        .verify();
  }

  @Test
  public void findByEmail() {
    this.customerRepository
        .findByEmail("mike@gmail.com")
        .doOnNext(c -> log.info("customer: {}", c))
        .as(StepVerifier::create)
        .assertNext(c -> Assertions.assertEquals("mike@gmail.com", c.getEmail()))
        .expectComplete()
        .verify();
  }

  @Test
  public void findByEmailEndingWith() {
    this.customerRepository
        .findByEmailEndingWith("ke@gmail.com")
        .doOnNext(c -> log.info("customer: {}", c))
        .as(StepVerifier::create)
        .assertNext(c -> Assertions.assertEquals("mike@gmail.com", c.getEmail()))
        .assertNext(c -> Assertions.assertEquals("jake@gmail.com", c.getEmail()))
        .expectComplete()
        .verify();
  }

  @Test
  public void insertAndDelete() {
    // inset
    var customer = new Customer();
    customer.setName("satya");
    customer.setEmail("ssyadav.in@gmail.com");

    this.customerRepository
        .save(customer)
        .doOnNext(c -> log.info("customer: {}", c))
        .as(StepVerifier::create)
        .assertNext(c -> Assertions.assertNotNull(c.getId()))
        .expectComplete()
        .verify();

    // count

    this.customerRepository
        .count()
        .as(StepVerifier::create)
        .expectNext(11L)
        .expectComplete()
        .verify();

    // delete
    this.customerRepository
        .deleteById(11)
        .then(this.customerRepository.count())
        .as(StepVerifier::create)
        .expectNext(10L)
        .expectComplete()
        .verify();
  }

  @Test
  public void updateCustomer() {
    this.customerRepository
        .findByName("ethan")
        .doOnNext(c -> c.setName("ethan updated"))
        .flatMap(this.customerRepository::save)
        .doOnNext(c -> log.info("customer: {}", c))
        .as(StepVerifier::create)
        .assertNext(c -> Assertions.assertEquals("ethan updated", c.getName()))
        .expectComplete()
        .verify();
  }
}
