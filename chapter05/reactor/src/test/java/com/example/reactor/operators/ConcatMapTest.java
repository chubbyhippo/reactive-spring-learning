package com.example.reactor.operators;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class ConcatMapTest {

	@AllArgsConstructor
	static class Pair {
		private int id;
		private long delay;
	}

	private Flux<Integer> delayReplayFor(Integer i, long delay) {
		return Flux.just(i).delayElements(Duration.ofMillis(delay));
	}

	@Test
	public void concatMap() {
		Flux<Integer> data = Flux
				.just(new Pair(1, 300), new Pair(2, 200), new Pair(3, 100))
				.concatMap(id -> this.delayReplayFor(id.id, id.delay));

		StepVerifier.create(data).expectNext(1, 2, 3).verifyComplete();

	}

}
