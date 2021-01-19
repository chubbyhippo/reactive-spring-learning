package com.example.reactor;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.test.StepVerifier;
// EmitterProcessor was deprecated
public class SinksTest {

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
		sinks.emitNext("1", null);
		sinks.emitNext("2", null);
		sinks.emitNext("3", null);
		sinks.emitComplete(null);
		consume(sinks.asFlux());
	}

}
