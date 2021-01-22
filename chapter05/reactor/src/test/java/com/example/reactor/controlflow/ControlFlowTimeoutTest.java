package com.example.reactor.controlflow;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class ControlFlowTimeoutTest {

	private Flux<Integer> given(Throwable t) {
		assertThat(t instanceof TimeoutException)
				.as("this exception should be a "
						+ TimeoutException.class.getName())
				.isTrue();

		return Flux.just(0);
	}

	@Test
	public void timeout() throws Exception {
		Flux<Integer> ids = Flux.just(1, 2, 3)
				.delayElements(Duration.ofSeconds(1))
				.timeout(Duration.ofMillis(500))
				.onErrorResume(this::given);

		StepVerifier.create(ids).expectNext(0).verifyComplete();
	}

}
