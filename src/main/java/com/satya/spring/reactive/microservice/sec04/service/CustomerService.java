package com.satya.spring.reactive.microservice.sec04.service;

import com.satya.spring.reactive.microservice.sec04.dto.CustomerDto;
import com.satya.spring.reactive.microservice.sec04.exceptions.ApplicationExceptions;
import com.satya.spring.reactive.microservice.sec04.mapper.CustomerEntitytDtoMapper;
import com.satya.spring.reactive.microservice.sec04.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CustomerService {
  public static final Logger log = LoggerFactory.getLogger(CustomerService.class);

  @Autowired private CustomerRepository customerRepository;

  public Flux<CustomerDto> getAllCustomer() {
    log.info("CustomerService.getAllCustomer: ");
    return this.customerRepository.findAll().map(CustomerEntitytDtoMapper::toDto);
  }

  public Mono<CustomerDto> getCustomerById(Integer id) {
    log.info("CustomerService.getAllCustomer: {}", id);
    return this.customerRepository.findById(id).map(CustomerEntitytDtoMapper::toDto);
  }

  public Mono<CustomerDto> saveCustomer(Mono<CustomerDto> customerDtoMono) {
    return customerDtoMono
        .map(CustomerEntitytDtoMapper::toEntity)
        .flatMap(this.customerRepository::save)
        .map(CustomerEntitytDtoMapper::toDto);
  }

  public Mono<CustomerDto> updateCustomer(Integer id, Mono<CustomerDto> mono) {
    return this.customerRepository
        .findById(id)
        .flatMap(entity -> mono)
        .map(CustomerEntitytDtoMapper::toEntity)
        .doOnNext(e -> e.setId(id))
        .flatMap(this.customerRepository::save)
        .map(CustomerEntitytDtoMapper::toDto);
  }

  public Mono<Boolean> deleteCustomerById(Integer id){
    return this.customerRepository.deleteCustomerById(id);
  }

  public Flux<CustomerDto> getAllCustomerByPageable(Integer pageNo, Integer pageSize) {
    return this.customerRepository.findBy(PageRequest.of(pageNo - 1, pageSize)).map(CustomerEntitytDtoMapper::toDto);
  }

  public Mono<Page<CustomerDto>> getAllCustomerByPageableWithCount(Integer pageNo, Integer pageSize) {
    var pageRequest = PageRequest.of(pageNo-1, pageSize);
    return this.customerRepository.findBy(pageRequest)
            .collectList()
            .zipWith(this.customerRepository.count())
            .map(t -> new PageImpl<>(t.getT1(),pageRequest, t.getT2()))
            .map(page -> page.map(CustomerEntitytDtoMapper::toDto));
  }
}
