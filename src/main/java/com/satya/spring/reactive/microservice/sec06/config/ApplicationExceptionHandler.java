package com.satya.spring.reactive.microservice.sec06.config;

import com.satya.spring.reactive.microservice.sec06.exceptions.CustomerNotFoundException;
import com.satya.spring.reactive.microservice.sec06.exceptions.InvalidInputException;
import java.net.URI;
import java.util.function.Consumer;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
public class ApplicationExceptionHandler {

  public Mono<ServerResponse> handleException(CustomerNotFoundException ex, ServerRequest request) {
      return handleException(HttpStatus.NOT_FOUND, ex, request, problem -> {
          problem.setType(URI.create("https://example.com/customer-not-found"));
          problem.setTitle("Customer Not Found");
      });
  }

  public Mono<ServerResponse> handleException(InvalidInputException ex, ServerRequest request) {
      return handleException(HttpStatus.BAD_REQUEST, ex, request, problem -> {
          problem.setType(URI.create("https://example.com/invalid-input"));
          problem.setTitle("Invalid Input");
      });
  }

  private Mono<ServerResponse> handleException(HttpStatus status, Exception ex, ServerRequest request,
                                              Consumer<ProblemDetail> consumer) {
    var problem = ProblemDetail.forStatusAndDetail(status, ex.getMessage());
    consumer.accept(problem);
    problem.setInstance(URI.create(request.path()));
    return ServerResponse.status(status).bodyValue(problem);
  }
}
