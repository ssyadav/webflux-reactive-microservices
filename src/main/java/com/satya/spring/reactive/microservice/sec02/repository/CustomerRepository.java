package com.satya.spring.reactive.microservice.sec02.repository;

import com.satya.spring.reactive.microservice.sec02.entity.Customer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface CustomerRepository extends ReactiveCrudRepository<Customer, Integer> {

  Flux<Customer> findByName(String name);
  Flux<Customer> findByEmail(String email);
  Flux<Customer> findByEmailEndingWith(String email);
}
