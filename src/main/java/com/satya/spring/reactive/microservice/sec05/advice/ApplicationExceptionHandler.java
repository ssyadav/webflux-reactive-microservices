package com.satya.spring.reactive.microservice.sec05.advice;


import com.satya.spring.reactive.microservice.sec05.exceptions.CustomerNotFoundException;
import com.satya.spring.reactive.microservice.sec05.exceptions.InvalidInputException;
import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException.class)
    public ProblemDetail handleExcepton(CustomerNotFoundException ex) {
        var problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setType(URI.create("https://example.com/customer-not-found"));
        problem.setTitle("Customer Not Found");
        return problem;
    }

    @ExceptionHandler(InvalidInputException.class)
    public ProblemDetail handleExcepton(InvalidInputException ex) {
        var problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problem.setType(URI.create("https://example.com/invalid-input"));
        problem.setTitle("Invalid Input");
        return problem;
    }
}
