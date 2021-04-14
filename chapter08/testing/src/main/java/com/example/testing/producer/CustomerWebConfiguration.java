package com.example.testing.producer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class CustomerWebConfiguration {

    @Bean
    RouterFunction<ServerResponse> routes(CustomerRepository customerRepository) {
        return RouterFunctions.route(GET("/customers"),
                request -> ok().body(customerRepository.findAll(), Customer.class));
    }
}
