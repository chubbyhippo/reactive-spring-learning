package com.example.webclient.client;

import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class AuthenticatedClient {

	private final WebClient client;

	public Mono<Greeting> getAuthenticatedGreeting() {
		return this.client
				.get()
				.uri("/greet/authenticated")
				.retrieve()
				.bodyToMono(Greeting.class);
	}

}
