package com.example.webflux.sse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.example.webflux.utils.IntervalMessageProducer;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

@Log4j2
@Configuration
public class SseConfiguration {

	private final String countPathVariable = "count";

	Mono<ServerResponse> handleSse(ServerRequest r) {
		var countPathVariable = Integer
				.parseInt(r.pathVariable(this.countPathVariable));
		var publisher = IntervalMessageProducer.produce(countPathVariable)
				.doOnComplete(() -> log.info("completed"));

		return ServerResponse.ok().contentType(MediaType.TEXT_EVENT_STREAM)
				.body(publisher, String.class);
	}

	@Bean
	RouterFunction<ServerResponse> routes() {
		return RouterFunctions.route()
				.GET("/sse/{" + countPathVariable + "}", this::handleSse)
				.build();
	}

}
