package com.example.webflux.routes;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class NestedFunctionalEndpointConfiguration {

	@Bean
	RouterFunction<ServerResponse> nested(NestedHandler nestedHandler) {

		var jsonRP = accept(APPLICATION_JSON);
		var sseRP = accept(TEXT_EVENT_STREAM);

		return RouterFunctions.route()
				.nest(path("/nested"),
						builder -> builder
								.nest(jsonRP, nestedBuilder -> nestedBuilder
										.GET("/{pv}",
												nestedHandler::pathVariable)
										.GET("", nestedHandler::noPathVariable))
								.add(RouterFunctions.route(sseRP,
										nestedHandler::sse)))
				.build();
	}

}
