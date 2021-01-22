package com.example.reactor.context;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Signal;
import reactor.core.publisher.SignalType;
import reactor.util.context.Context;
import reactor.util.context.ContextView;

@Log4j2
public class ContextTest {

	@Test
	public void context() throws Exception {
		var observedContextValues = new ConcurrentHashMap<String, AtomicInteger>();
		var max = 3;
		var key = "key1";
		var cdl = new CountDownLatch(max);
		Context context = Context.of(key, "value1");
		Flux<Integer> just = Flux.range(0, max)
				.delayElements(Duration.ofMillis(1))
				.doOnEach((Signal<Integer> integerSignal) -> {
					ContextView currentContext = integerSignal.getContextView();
					if (integerSignal.getType().equals(SignalType.ON_NEXT)) {
						String key1 = currentContext.get(key);

						assertThat(key1).isNotNull();
						assertThat(key1).isEqualTo("value1");

						observedContextValues
								.computeIfAbsent("key1",
										k -> new AtomicInteger(0))
								.incrementAndGet();
					}
				}).contextWrite(context);

		just.subscribe(integer -> {
			log.info("integer: " + integer);
			cdl.countDown();
		});

		cdl.await();

		assertThat(observedContextValues.get(key).get()).isEqualTo(max);

	}

}
