package com.satya.spring.reactive.microservice.sec03.controller;

import com.satya.spring.reactive.microservice.sec03.dto.CustomerDto;
import com.satya.spring.reactive.microservice.sec03.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("customers")
public class CustomerController {

  public static final Logger log = LoggerFactory.getLogger(CustomerController.class);

  @Autowired private CustomerService customerService;

  @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<CustomerDto> getAllCustomers() {
    log.info("CustomerController.getAllCustomers: ");
    return this.customerService.getAllCustomer();
  }

  @GetMapping(value = "{id}")
  public Mono<ResponseEntity<CustomerDto>> getCustomerById(@PathVariable Integer id) {
    log.info("CustomerController.getCustomerById: {}", id);
    return this.customerService.getCustomerById(id)
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @GetMapping(value = "pageable")
  public Flux<CustomerDto> getAllCustomerByPageable(
          @RequestParam(defaultValue = "1") Integer pageNo,
          @RequestParam(defaultValue = "3") Integer pageSize) {
    log.info("CustomerController.getAllCustomerByPageable: {} {}", pageNo, pageSize);
    return this.customerService.getAllCustomerByPageable(pageNo, pageSize);
  }

  @GetMapping(value = "pagecount")
  public Mono<Page<CustomerDto>> getAllCustomerByPageableWithCount(
          @RequestParam(defaultValue = "1") Integer pageNo,
          @RequestParam(defaultValue = "3") Integer pageSize) {
    log.info("CustomerController.getAllCustomerByPageableWithCount: {} {}", pageNo, pageSize);
    return this.customerService.getAllCustomerByPageableWithCount(pageNo, pageSize);
  }

  @PostMapping
  public Mono<CustomerDto> saveCustomer(@RequestBody Mono<CustomerDto> mono) {
    log.info("CustomerController.saveCustomer: ");
    return this.customerService.saveCustomer(mono);
  }

  @PutMapping("{id}")
  public Mono<ResponseEntity<CustomerDto>> updateCustomer(
      @PathVariable Integer id, @RequestBody Mono<CustomerDto> mono) {
    log.info("CustomerController.updateCustomer: ");
    return this.customerService.updateCustomer(id, mono)
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @DeleteMapping("{id}")
  public Mono<ResponseEntity<Void>> deleteCustomer(@PathVariable Integer id) {
    log.info("CustomerController.deleteCustomer: ");
    return this.customerService.deleteCustomer(id);
  }
}
