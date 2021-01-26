package com.example.webflux.customers;

import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class CustomerViewEndpointConfiguration {

	@Bean
	RouterFunction<ServerResponse> customerViews(
			CustomerRepository repository) {
		return RouterFunctions.route().GET("/fn/customer.php", r -> {
			var map = Map.of("customer", repository.findAll(),
					"type", "Functional Reactive");
			return ServerResponse.ok().render("customers", map);
		}).build();
	}

}
