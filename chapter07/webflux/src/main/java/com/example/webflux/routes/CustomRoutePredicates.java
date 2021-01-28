package com.example.webflux.routes;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class CustomRoutePredicates {

	private final HandlerFunction<ServerResponse> handler = request -> ok().bodyValue("Hello, "
					+ request.queryParam("name").orElse("world") + "!");

	boolean isRequestForAValidUid(ServerRequest request) {
		var goodUids = Set.of("1", "2", "3");
		return request.queryParam("uid")
				.map(goodUids::contains)
				.orElse(false);
	}

	@Bean
	RouterFunction<ServerResponse> customRequestPredicates() {
		var aPeculiarRequestPredicate = GET("/test")
				.and(RequestPredicates.accept(APPLICATION_JSON))
				.and(this::isRequestForAValidUid);

		var caseInsensitiveRequestPredicate = CaseInsensitiveRequestPredicate
				.i(GET("/greetings/{name}"));
		
		return RouterFunctions.route()
				.add(RouterFunctions.route(aPeculiarRequestPredicate,
						handler))
				.add(RouterFunctions.route(caseInsensitiveRequestPredicate,
						handler))
				.build();
	}

}
