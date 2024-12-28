package com.satya.spring.reactive.microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@SpringBootApplication(scanBasePackages = "com.satya.spring.reactive.microservice.${sec}")
@EnableR2dbcRepositories(basePackages = "com.satya.spring.reactive.microservice.${sec}")
public class SpringReactiveMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringReactiveMicroserviceApplication.class, args);
	}

}
