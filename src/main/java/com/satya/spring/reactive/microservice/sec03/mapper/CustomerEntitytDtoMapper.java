package com.satya.spring.reactive.microservice.sec03.mapper;


import com.satya.spring.reactive.microservice.sec03.dto.CustomerDto;
import com.satya.spring.reactive.microservice.sec03.entity.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomerEntitytDtoMapper {

    public static final Logger log = LoggerFactory.getLogger(CustomerEntitytDtoMapper.class);

    public static Customer toEntity(CustomerDto dto) {
        log.info("Mapping CustomerDto to Customer");
        var customer = new Customer();
        customer.setId(dto.id());
        customer.setName(dto.name());
        customer.setEmail(dto.email());
        return customer;
    }

    public static CustomerDto toDto(Customer customer) {
        log.info("Mapping Customer to CustomerDto");
        return new CustomerDto(customer.getId(), customer.getName(), customer.getEmail());
    }


}
