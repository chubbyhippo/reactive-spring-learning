package com.example.reactor.operators;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@Log4j2
public class FlatMapTest {

	@AllArgsConstructor
	static class Pair {
		private int id;
		private long delay;
	}

	private Flux<Integer> delayReplyFor(Integer i, long delay) {
		return Flux.just(i).delayElements(Duration.ofMillis(delay));
	}

	@Test
	public void flatMap() {

		Flux<Integer> data = Flux.just(new Pair(1, 300), new Pair(2, 200),
				new Pair(3, 100))
				.flatMap(id -> this.delayReplyFor(id.id, id.delay));

		StepVerifier.create(data).expectNext(3, 2, 1).verifyComplete();
	}

}
