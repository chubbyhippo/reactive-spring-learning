package com.example.webflux.utils;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicLong;

import reactor.core.publisher.Flux;

public abstract class IntervalMessageProducer {

	private static Flux<CountAndString> doProduceCountAndStrings() {
		var counter = new AtomicLong();
		return Flux.interval(Duration.ofSeconds(1))
				.map(i -> new CountAndString(counter.incrementAndGet()));
	}

	public static Flux<String> produce() {
		return doProduceCountAndStrings().map(CountAndString::getMessage);
	}

	public static Flux<String> produce(int c) {
		return produce().take(c);
	}

}
