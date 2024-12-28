package com.satya.spring.reactive.microservice.sec06.exceptions;


public class CustomerNotFoundException extends RuntimeException{

    public static final String MESSAGE = "Customer [id=%d] is not found";
    public CustomerNotFoundException(Integer id) {
        super(MESSAGE.formatted(id));
    }
}