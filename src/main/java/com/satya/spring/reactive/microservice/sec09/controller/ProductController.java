package com.satya.spring.reactive.microservice.sec09.controller;

import com.satya.spring.reactive.microservice.sec09.dto.ProductDto;
import com.satya.spring.reactive.microservice.sec09.dto.UploadResponse;
import com.satya.spring.reactive.microservice.sec09.service.ProductService;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("products")
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    @Autowired private ProductService service;

    @PostMapping
    public Mono<ProductDto> uploadProducts(@RequestBody Mono<ProductDto> mono) {
        log.info("invoked");
        return this.service.saveProduct(mono);
    }

    @GetMapping(value = "stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ProductDto> getProducts(){
        return this.service.getProducts();
    }

    @GetMapping(value = "/stream/{maxPrice}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ProductDto> getProducts(@PathVariable int maxPrice){
        return this.service.getProducts(maxPrice);
    }

}