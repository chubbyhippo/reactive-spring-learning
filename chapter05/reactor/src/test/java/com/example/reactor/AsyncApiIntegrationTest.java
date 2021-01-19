package com.example.reactor;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.test.StepVerifier;

@Log4j2
public class AsyncApiIntegrationTest {

	private final ExecutorService executorService = Executors
			.newFixedThreadPool(1);

	private void launch(FluxSink<Integer> integerFluxSink, int count) {
		this.executorService.submit(() -> {
			var integer = new AtomicInteger();
			assertThat(integerFluxSink).isNotNull();
			while (integer.get() < count) {
				double random = Math.random();

				integerFluxSink.next(integer.incrementAndGet());
				this.sleep((long) (random * 1_000));
			}
			integerFluxSink.complete();
		});

	}

	private void sleep(long s) {
		try {
			Thread.sleep(s);
		} catch (Exception e) {
			log.error(e);
		}
	}

	@Test
	public void async() {

		Flux<Integer> integers = Flux
				.create(emitter -> this.launch(emitter, 5));

		StepVerifier
				.create(integers.doFinally(
						signalType -> this.executorService.shutdown()))
				.expectNextCount(5).verifyComplete();

	}

}
