package com.example.reactor.debugging;

import java.util.ArrayList;
import java.util.ServiceLoader;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import lombok.SneakyThrows;
import reactor.blockhound.BlockHound;
import reactor.blockhound.integration.BlockHoundIntegration;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

@TestInstance(Lifecycle.PER_CLASS)
public class BlockHoundTest {

	private final static AtomicBoolean BLOCKHOUND = new AtomicBoolean();

	private static class BlockingCallError extends Error {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public BlockingCallError(String msg) {
			super(msg);
		}

	}

	@BeforeAll
	public void beforeAll() {
		BLOCKHOUND.set(true);
		var integrations = new ArrayList<BlockHoundIntegration>();
		var services = ServiceLoader.load(BlockHoundIntegration.class);
		services.forEach(integrations::add);

		integrations.add(
				builder -> builder.blockingMethodCallback(blockingMethod -> {
					if (BLOCKHOUND.get()) {
						throw new BlockingCallError(blockingMethod.toString());

					}
				}));

		BlockHound.install(integrations.toArray(new BlockHoundIntegration[0]));
	}

	@AfterAll
	public void afterAll() {
		BLOCKHOUND.set(false);
	}

	Mono<Long> buildBlockingMono() {
		return Mono.just(1L).doOnNext(it -> block());
	}

	@SneakyThrows
	void block() {
		Thread.sleep(1000);
	}

	@Test
	public void notOk() {
		StepVerifier
				.create(buildBlockingMono().subscribeOn(Schedulers.parallel()))
				.expectErrorMatches(
						e -> e instanceof BlockingCallError
				).verify();
	}
	
	@Test
	public void ok() {
		StepVerifier.create(buildBlockingMono().subscribeOn(Schedulers.boundedElastic()))
		.expectNext(1L)
		.verifyComplete();
	}

}
