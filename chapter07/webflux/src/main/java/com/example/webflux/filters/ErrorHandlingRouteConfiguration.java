package com.example.webflux.filters;

import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

@Configuration
public class ErrorHandlingRouteConfiguration {

	@Bean
	RouterFunction<ServerResponse> errors() {
		var productIdPathVariable = "productId";

		return RouterFunctions.route()
				.GET("/products/{" + productIdPathVariable + "}", request -> {
					var productId = request.pathVariable(productIdPathVariable);
					if (!Set.of("1", "2").contains(productId)) {
						return ServerResponse.ok()
								.bodyValue(new Product(productId));
					} else {
						return Mono
								.error(new ProductNotFoundException(productId));
					}
				}) //
				.filter((request, next) -> next.handle(request) // <1>
						.onErrorResume(ProductNotFoundException.class,
								pnfe -> ServerResponse.notFound().build())) // <2>
				.build();
	}

}
