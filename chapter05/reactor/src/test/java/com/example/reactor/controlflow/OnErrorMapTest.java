package com.example.reactor.controlflow;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class OnErrorMapTest {

	@Test
	public void onErrorMap() throws Exception {
		class GenericException extends RuntimeException {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
		};

		var counter = new AtomicInteger();
		Flux<Integer> resultsInError = Flux
				.error(new IllegalArgumentException("opps!"));
		Flux<Integer> errorHandlingStream = resultsInError
				.onErrorMap(IllegalArgumentException.class,
						ex -> new GenericException())
				.doOnError(GenericException.class,
						ge -> counter.incrementAndGet());

		StepVerifier.create(errorHandlingStream).expectError().verify();
		assertThat(counter.get()).isEqualTo(1);

	}

}
