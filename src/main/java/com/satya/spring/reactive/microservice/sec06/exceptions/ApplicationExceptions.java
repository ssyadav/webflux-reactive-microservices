package com.satya.spring.reactive.microservice.sec06.exceptions;


import reactor.core.publisher.Mono;

public class ApplicationExceptions {

    public static <T> Mono<T> customerNotFoundException(Integer id) {
        return Mono.error(new CustomerNotFoundException(id));
    }

    public static <T> Mono<T> missingName() {
    return Mono.error(new InvalidInputException("Name must not be null"));
    }

    public static <T> Mono<T> missingValidEmail() {
        return Mono.error(new InvalidInputException("Email must be valid"));
    }
}
