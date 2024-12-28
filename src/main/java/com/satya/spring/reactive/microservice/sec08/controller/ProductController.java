package com.satya.spring.reactive.microservice.sec08.controller;

import com.satya.spring.reactive.microservice.sec08.dto.ProductDto;
import com.satya.spring.reactive.microservice.sec08.dto.UploadResponse;
import com.satya.spring.reactive.microservice.sec08.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("products")
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    @Autowired private ProductService service;

    @PostMapping(value = "upload", consumes = MediaType.APPLICATION_NDJSON_VALUE)
    public Mono<UploadResponse> uploadProducts(@RequestBody Flux<ProductDto> flux) {
        log.info("invoked");
        return this.service.saveProducts(flux)//.doOnNext(p -> log.info("received: {}", p))
                .then(this.service.getProductsCount())
                .map(count -> new UploadResponse(UUID.randomUUID(), count));
    }

    @GetMapping(value = "download", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<ProductDto> downloadProducts(){
        return this.service.allProducts();
    }

}