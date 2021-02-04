package com.example.webclient.service;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.Stream;

import org.reactivestreams.Publisher;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.webclient.client.Greeting;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
class HttpController {

	@GetMapping(value = "/greet/single/{name}")
	Publisher<Greeting> greetSingle(@PathVariable String name) {
		return Mono.just(greeting(name));
	}

	@GetMapping(value = "/greet/many/{name}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	Publisher<Greeting> greetMany(@PathVariable String name) {
		return Flux
				.fromStream(Stream.generate(() -> greeting(name)))
				.delayElements(Duration.ofSeconds(1));
	}

	private Greeting greeting(String name) {
		return new Greeting("Hello " + name + " @ " + Instant.now());
	}

}
