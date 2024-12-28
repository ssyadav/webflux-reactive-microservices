package com.satya.spring.reactive.microservice.sec06.config;

import com.satya.spring.reactive.microservice.sec06.dto.CustomerDto;
import com.satya.spring.reactive.microservice.sec06.exceptions.ApplicationExceptions;
import com.satya.spring.reactive.microservice.sec06.service.CustomerService;
import com.satya.spring.reactive.microservice.sec06.validator.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
public class CustomerRequestHandler {

  @Autowired private CustomerService customerService;

  public Mono<ServerResponse> allCustomers(ServerRequest request) {
    // request.pathVariable("id");
    // request.headers();
    // request.queryParams()
    return this.customerService
        .getAllCustomer()
        .as(flux -> ServerResponse.ok().body(flux, CustomerDto.class));
  }

    public Mono<ServerResponse> paginatedCustomers(ServerRequest request) {
      var pageNo = request.queryParam("pageNo").map(Integer::parseInt).orElse(1);
      var pageSize = request.queryParam("pageSize").map(Integer::parseInt).orElse(3);
      return this.customerService
                .getAllCustomerByPageable(pageNo, pageSize)
                .collectList()
                .flatMap(ServerResponse.ok()::bodyValue);
    }

  public Mono<ServerResponse> getCustomer(ServerRequest request) {
    var id = Integer.parseInt(request.pathVariable("id"));
    return this.customerService
        .getCustomerById(id)
        .switchIfEmpty(ApplicationExceptions.customerNotFoundException(id))
        .flatMap(ServerResponse.ok()::bodyValue);
  }

  public Mono<ServerResponse> saveCustomer(ServerRequest request) {
    return request
        .bodyToMono(CustomerDto.class)
        .transform(RequestValidator.validate())
        .as(this.customerService::saveCustomer)
        .flatMap(ServerResponse.ok()::bodyValue);
  }

  public Mono<ServerResponse> updateCustomer(ServerRequest request) {
    var id = Integer.parseInt(request.pathVariable("id"));
    return request
        .bodyToMono(CustomerDto.class)
        .transform(RequestValidator.validate())
        .as(validReq -> this.customerService.updateCustomer(id, validReq))
        .switchIfEmpty(ApplicationExceptions.customerNotFoundException(id))
        .flatMap(ServerResponse.ok()::bodyValue);
  }

    public Mono<ServerResponse> deleteCustomer(ServerRequest request) {
        var id = Integer.parseInt(request.pathVariable("id"));
        return this.customerService
                .deleteCustomerById(id)
                .filter(Boolean::booleanValue)
                .switchIfEmpty(ApplicationExceptions.customerNotFoundException(id))
                .then(ServerResponse.ok().build());
    }
}
