package com.example.webflux.filters;

import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

@Log4j2
@Configuration
public class LowercaseWebConfiguration {
	Mono<ServerResponse> handle(ServerRequest serverRequest) {
		return ServerResponse.ok().bodyValue(
				String.format("Hello, %s!",
						serverRequest.pathVariable("name")));
	}

	@Bean
	RouterFunction<ServerResponse> routerFunctionFilters() {
		var uuidKey = UUID.class.getName();
		return RouterFunctions.route()
				.GET("/hi/{name}", this::handle)
				.GET("/hello/{name}", this::handle)
				.filter((req, next) -> {
					log.info(".filter(): before");
					var reply = next.handle(req);
					log.info(".filter(): after");
					return reply;
				})
				.before(request -> {
					log.info(".before()");
					request.attributes().put(uuidKey, UUID.randomUUID());
					return request;
				})
				.after((serverRequest, serverResponse) -> {
					log.info(".after()");
					log.info(
							"UUID: " + serverRequest.attributes().get(uuidKey));
					return serverResponse;
				})
				.onError(NullPointerException.class,
						(e, request) -> ServerResponse.badRequest().build())
				.build();
	}
}
