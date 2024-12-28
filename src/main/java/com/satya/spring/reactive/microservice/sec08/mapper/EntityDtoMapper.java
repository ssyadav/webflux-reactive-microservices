package com.satya.spring.reactive.microservice.sec08.mapper;


import com.satya.spring.reactive.microservice.sec08.dto.ProductDto;
import com.satya.spring.reactive.microservice.sec08.entity.Product;

public class EntityDtoMapper {

    public static Product toEntity(ProductDto dto){
        var product = new Product();
        product.setId(dto.id());
        product.setDescription(dto.description());
        product.setPrice(dto.price());
        return product;
    }

    public static ProductDto toDto(Product product){
        return new ProductDto(
                product.getId(),
                product.getDescription(),
                product.getPrice()
        );
    }
}