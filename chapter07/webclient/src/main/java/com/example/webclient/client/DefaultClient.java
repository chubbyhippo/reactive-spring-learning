package com.example.webclient.client;

import java.util.Map;

import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class DefaultClient {

	private final WebClient client;

	public Mono<Greeting> getSingle(String name) {
		return client.get()
				.uri("/greet/single/{name}", Map.of("name", name))// "Stéphane Maldini"
				.retrieve()
				.bodyToMono(Greeting.class);
	}

	public Flux<Greeting> getMany(String name) {
		return client.get()
				.uri("/greet/many/{name}", Map.of("name", name))// "Madhura Bhave"
				.retrieve()
				.bodyToFlux(Greeting.class)
				.take(10);
	}

}