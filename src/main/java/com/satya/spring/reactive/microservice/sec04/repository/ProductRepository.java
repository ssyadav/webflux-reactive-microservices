package com.satya.spring.reactive.microservice.sec04.repository;

import com.satya.spring.reactive.microservice.sec04.entity.Product;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product, Integer> {

}
