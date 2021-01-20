package com.example.reactor.operators;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class TransformTest {

	@Test
	public void transform() {
		var finished = new AtomicBoolean();
		var letters = Flux.just("A", "B", "C")
				.transform(stringFlux -> stringFlux
						.doFinally(signal -> finished.set(true)));
		
		StepVerifier.create(letters).expectNextCount(3).verifyComplete();
		assertThat(finished.get()).isTrue();
	}

}
