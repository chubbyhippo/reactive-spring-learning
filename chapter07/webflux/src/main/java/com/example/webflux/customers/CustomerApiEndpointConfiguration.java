package com.example.webflux.customers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class CustomerApiEndpointConfiguration {

	@Bean
	RouterFunction<ServerResponse> customerApis(CustomerHandler handler) {
		return RouterFunctions.route()
				.nest(RequestPredicates.path("fn/customers"), builder -> builder
						.GET("/{id}", handler::handleFindCustomerById)
						.GET("", handler::handleFindAll)
						.POST("", handler::handleCreateCustomer))
				.build();
	}

}
