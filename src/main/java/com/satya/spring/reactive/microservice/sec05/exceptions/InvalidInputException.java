package com.satya.spring.reactive.microservice.sec05.exceptions;

public class InvalidInputException extends RuntimeException {

  public InvalidInputException(String message) {
    super(message);
  }
}
