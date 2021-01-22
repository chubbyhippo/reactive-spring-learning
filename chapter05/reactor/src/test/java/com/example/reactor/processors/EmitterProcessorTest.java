package com.example.reactor.processors;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.test.StepVerifier;
// EmitterProcessor was deprecated
public class EmitterProcessorTest {

	private void consume(Flux<String> publisher) {
		StepVerifier.create(publisher)
				.expectNext("1")
				.expectNext("2")
				.expectNext("3")
				.verifyComplete();
	}

	@Test
	public void sinks() {
		Sinks.Many<String> sinks = Sinks.many().multicast().onBackpressureBuffer();
		sinks.tryEmitNext("1");
		sinks.tryEmitNext("2");
		sinks.tryEmitNext("3");
		sinks.tryEmitComplete();
		consume(sinks.asFlux());
	}

}
