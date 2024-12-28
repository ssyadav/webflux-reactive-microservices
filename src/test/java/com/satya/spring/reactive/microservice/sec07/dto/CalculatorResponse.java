package com.satya.spring.reactive.microservice.sec07.dto;


public record CalculatorResponse(Integer first,
                                 Integer second,
                                 String operation,
                                 Double result) {}
