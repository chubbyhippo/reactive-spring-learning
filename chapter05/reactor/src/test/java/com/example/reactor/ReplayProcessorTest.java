package com.example.reactor;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.test.StepVerifier;

public class ReplayProcessorTest {

	private void consume(Flux<String> publisher) {
		for (int i = 0; i < 5; i++) {
			StepVerifier.create(publisher)
					.expectNext("2")
					.expectNext("3")
					.verifyComplete();
		}
	}

	@Test
	public void replayProcessor() {
		int historySize = 2;
		boolean unbounded = false;

		Sinks.Many<String> sinks = Sinks.many().replay().limit(historySize);
		sinks.emitNext("1", null);
		sinks.emitNext("2", null);
		sinks.emitNext("3", null);
		sinks.emitComplete(null);

		consume(sinks.asFlux());

	}

}
