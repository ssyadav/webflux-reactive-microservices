package com.satya.spring.reactive.microservice.sec06.exceptions;

public class InvalidInputException extends RuntimeException {

  public InvalidInputException(String message) {
    super(message);
  }
}
