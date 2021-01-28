package com.example.webflux.routes;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class SimpleFunctionalEndpointConfiguration {

	@Bean
	RouterFunction<ServerResponse> simple(GreetingsHandlerFunction handler) {

		return RouterFunctions.route()
				.GET("/hello/{name}", request -> {
					var namePathVariable = request.pathVariable("name");
					var message = String.format("Hello %s!", namePathVariable);
					return ServerResponse.ok().bodyValue(message);
				})
				.GET("/hodor", handler)
				.GET("/sup", handler::handle)
				.build();
	}

}
