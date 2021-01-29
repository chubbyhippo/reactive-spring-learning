package com.example.webflux.utils;

import org.junit.jupiter.api.Test;

import reactor.test.StepVerifier;

class IntervalMessageProducerTest {

	@Test
	public void produce() {

		var res = IntervalMessageProducer.produce(2);

		StepVerifier.create(res).expectNext("# 1").expectNext("# 2")
				.verifyComplete();
	}

}
