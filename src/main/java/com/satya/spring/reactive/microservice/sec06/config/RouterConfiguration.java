package com.satya.spring.reactive.microservice.sec06.config;

import com.satya.spring.reactive.microservice.sec06.exceptions.CustomerNotFoundException;
import com.satya.spring.reactive.microservice.sec06.exceptions.InvalidInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterConfiguration {

    @Autowired private CustomerRequestHandler customerRequestHendler;
    @Autowired private ApplicationExceptionHandler applicationExceptionHandler;


    @Bean
    public RouterFunction<ServerResponse> customerRoutes() {
        return RouterFunctions.route()
                .path("/customers", this::customerGetRoutes)
                .POST("/customers", this.customerRequestHendler::saveCustomer)
                .PUT("/customers/{id}", this.customerRequestHendler::updateCustomer)
                .DELETE("/customers/{id}", this.customerRequestHendler::deleteCustomer)
                .onError(CustomerNotFoundException.class, this.applicationExceptionHandler::handleException)
                .onError(InvalidInputException.class, this.applicationExceptionHandler::handleException)
                .build();
    }



    private RouterFunction<ServerResponse> customerGetRoutes() {
        return RouterFunctions.route()
                .GET("/pageable", req -> this.customerRequestHendler.paginatedCustomers(req))
                .GET("/{id}", this.customerRequestHendler::getCustomer)
                .GET(this.customerRequestHendler::allCustomers)
                .build();
    }


    /*@Bean
    public RouterFunction<ServerResponse> customerGetRoutes() {
        return RouterFunctions.route()
                .GET("/customers", req -> this.customerRequestHendler.allCustomers(req))
                .GET("/customers/pageable", req -> this.customerRequestHendler.paginatedCustomers(req))
                .GET("/customers/{id}", this.customerRequestHendler::getCustomer)
                .onError(CustomerNotFoundException.class, this.applicationExceptionHandler::handleException)
                .onError(InvalidInputException.class, this.applicationExceptionHandler::handleException)
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> customerPostRoutes() {
        return RouterFunctions.route()
                .POST("/customers", this.customerRequestHendler::saveCustomer)
                .PUT("/customers/{id}", this.customerRequestHendler::updateCustomer)
                .DELETE("/customers/{id}", this.customerRequestHendler::deleteCustomer)
                .onError(CustomerNotFoundException.class, this.applicationExceptionHandler::handleException)
                .onError(InvalidInputException.class, this.applicationExceptionHandler::handleException)
                .build();
    }*/

    /*@Bean
    public RouterFunction<ServerResponse> customerRoutes() {
        //.GET("/customers", this.customerRequestHendler::getCustomer)
        return RouterFunctions.route()
                .GET("/customers", req -> this.customerRequestHendler.allCustomers(req))
                .GET("/customers/pageable", req -> this.customerRequestHendler.paginatedCustomers(req))
                .GET("/customers/{id}", this.customerRequestHendler::getCustomer)
                .POST("/customers", this.customerRequestHendler::saveCustomer)
                .PUT("/customers/{id}", this.customerRequestHendler::updateCustomer)
                .DELETE("/customers/{id}", this.customerRequestHendler::deleteCustomer)
                .onError(CustomerNotFoundException.class, this.applicationExceptionHandler::handleException)
                .onError(InvalidInputException.class, this.applicationExceptionHandler::handleException)
                .build();
    }*/
}
