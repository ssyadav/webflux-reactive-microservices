package com.satya.spring.reactive.microservice.sec05.controller;

import com.satya.spring.reactive.microservice.sec05.dto.CustomerDto;
import com.satya.spring.reactive.microservice.sec05.exceptions.ApplicationExceptions;
import com.satya.spring.reactive.microservice.sec05.filter.Category;
import com.satya.spring.reactive.microservice.sec05.service.CustomerService;
import com.satya.spring.reactive.microservice.sec05.validator.RequestValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("customers")
public class CustomerController {

  public static final Logger log = LoggerFactory.getLogger(CustomerController.class);

  @Autowired private CustomerService customerService;

  @GetMapping()
  public Flux<CustomerDto> getAllCustomers() {
    log.info("CustomerController.getAllCustomers: ");
    return this.customerService.getAllCustomer();
  }

  /*@GetMapping()
  public Flux<CustomerDto> getAllCustomers(@RequestAttribute("category") Category category) {
    log.info("CustomerController.getAllCustomers: {}", category);
    return this.customerService.getAllCustomer();
  }*/

  @GetMapping(value = "{id}")
  public Mono<CustomerDto> getCustomerById(@PathVariable Integer id) {
    log.info("CustomerController.getCustomerById: {}", id);
    return this.customerService.getCustomerById(id)
            .switchIfEmpty(ApplicationExceptions.customerNotFoundException(id));
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
    /*var validatedMono = mono.transform(RequestValidator.validate());
    return this.customerService.saveCustomer(validatedMono);*/
    return mono.transform(RequestValidator.validate())
            .as(this.customerService::saveCustomer);
  }

  @PutMapping("{id}")
  public Mono<CustomerDto> updateCustomer(
      @PathVariable Integer id, @RequestBody Mono<CustomerDto> mono) {
    log.info("CustomerController.updateCustomer: ");
    return mono.transform(RequestValidator.validate())
            .as(validReq -> this.customerService.updateCustomer(id, validReq))
            .switchIfEmpty(ApplicationExceptions.customerNotFoundException(id));
  }

  @DeleteMapping("{id}")
  public Mono<Void> deleteCustomer(@PathVariable Integer id) {
    log.info("CustomerController.deleteCustomer: ");
    return this.customerService
        .deleteCustomerById(id)
        .filter(f -> f)
        .switchIfEmpty(ApplicationExceptions.customerNotFoundException(id))
        .then();
  }
}
