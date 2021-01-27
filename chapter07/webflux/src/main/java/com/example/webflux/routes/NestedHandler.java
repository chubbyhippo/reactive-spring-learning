package com.example.webflux.routes;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.example.webflux.utils.IntervalMessageProducer;

import reactor.core.publisher.Mono;

@Component
public class NestedHandler {

	public Mono<ServerResponse> sse(ServerRequest r) {
		return ServerResponse.ok().contentType(MediaType.TEXT_EVENT_STREAM)
				.body(IntervalMessageProducer.produce(), String.class);
	}

	private Map<String, String> greet(Optional<String> name) {
		var finalName = name.orElse("world");

		return Map.of("message", String.format("Hello %s!", finalName));
	}

	public Mono<ServerResponse> pathVariable(ServerRequest r) {
		return ServerResponse.ok()
				.bodyValue(greet(Optional.of(r.pathVariable("pv"))));
	}

	public Mono<ServerResponse> noPathVariable(ServerRequest r) {
		return ServerResponse.ok()
				.bodyValue(greet(Optional.ofNullable(null)));

	}

}
