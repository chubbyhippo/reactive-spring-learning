package com.example.reactor.schedulersandthreads;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

@Log4j2
public class SchedulersSubscribeOnTest {

	@Test
	public void subscribeOn() {
		var exampleThreadName = SchedulersSubscribeOnTest.class.getName();
		var map = new ConcurrentHashMap<String, AtomicInteger>();
		var executor = Executors.newFixedThreadPool(5, runnable -> {
			Runnable wrapper = () -> {
				var key = Thread.currentThread().getName();
				var result = map.computeIfAbsent(key, s -> new AtomicInteger());
				result.incrementAndGet();
				runnable.run();
			};
			return new Thread(wrapper, exampleThreadName);
		});
		Scheduler scheduler = Schedulers.fromExecutor(executor);

		Mono<Integer> integerFlux = Mono.just(1).subscribeOn(scheduler)
				.doFinally(
						signal -> map.forEach((k, v) -> log.info(k + '=' + v)));

		StepVerifier.create(integerFlux).expectNextCount(1).verifyComplete();
		var atomicInteger = map.get(exampleThreadName);
		assertThat(atomicInteger.get()).isEqualTo(1);
	}

}
