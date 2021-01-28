package com.example.webflux.routes;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

@Component
public class GreetingsHandlerFunction
		implements
			HandlerFunction<ServerResponse> {

	@Override
	public Mono<ServerResponse> handle(ServerRequest request) {
		return ServerResponse.ok().bodyValue("Hodor!");
	}

}
