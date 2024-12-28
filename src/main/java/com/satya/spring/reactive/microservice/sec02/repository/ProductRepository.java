package com.satya.spring.reactive.microservice.sec02.repository;

import com.satya.spring.reactive.microservice.sec02.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product, Integer> {

    Flux<Product> findByPriceBetween(Integer range1, Integer range2);

    Flux<Product> findBy(Pageable pageable);
}
