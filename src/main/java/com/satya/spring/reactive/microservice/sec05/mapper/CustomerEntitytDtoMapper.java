package com.satya.spring.reactive.microservice.sec05.mapper;


import com.satya.spring.reactive.microservice.sec05.dto.CustomerDto;
import com.satya.spring.reactive.microservice.sec05.entity.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomerEntitytDtoMapper {

    public static final Logger log = LoggerFactory.getLogger(CustomerEntitytDtoMapper.class);

    public static Customer toEntity(CustomerDto dto) {
        var customer = new Customer();
        customer.setId(dto.id());
        customer.setName(dto.name());
        customer.setEmail(dto.email());
        return customer;
    }

    public static CustomerDto toDto(Customer customer) {
        return new CustomerDto(customer.getId(), customer.getName(), customer.getEmail());
    }


}
