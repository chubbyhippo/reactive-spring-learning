package com.example.reactor.operators;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Signal;
import reactor.core.publisher.SignalType;
import reactor.test.StepVerifier;

@Log4j2
public class DoOnTest {

	@Test
	public void doOn() {
		var signals = new ArrayList<Signal<Integer>>();
		var nextValues = new ArrayList<Integer>();
		var subscriptions = new ArrayList<Subscription>();
		var exceptions = new ArrayList<Throwable>();
		var finallySignals = new ArrayList<SignalType>();

		Flux<Integer> on = Flux.<Integer>create(sink -> {
			sink.next(1);
			sink.next(2);
			sink.next(3);
			sink.error(new IllegalArgumentException("oops!"));
			sink.complete();
		})
				.doOnNext(nextValues::add)
				.doOnEach(signals::add)
				.doOnSubscribe(subscriptions::add)
				.doOnError(IllegalArgumentException.class, exceptions::add)
				.doFinally(finallySignals::add);

		StepVerifier.create(on).expectNext(1, 2, 3)
				.expectError(IllegalArgumentException.class).verify();

		signals.forEach(log::info);
		assertThat(signals.size()).isEqualTo(4);

		finallySignals.forEach(log::info);
		assertThat(finallySignals.size()).isEqualTo(1);

	}

}
