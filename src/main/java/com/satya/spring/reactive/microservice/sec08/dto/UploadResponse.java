package com.satya.spring.reactive.microservice.sec08.dto;


import java.util.UUID;

public record UploadResponse(UUID confirmationId,
                             Long productsCount) {}