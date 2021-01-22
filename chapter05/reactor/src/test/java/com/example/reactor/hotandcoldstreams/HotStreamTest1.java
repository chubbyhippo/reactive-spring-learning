package com.example.reactor.hotandcoldstreams;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Sinks;
import reactor.test.StepVerifier;

public class HotStreamTest1 {

	Consumer<Integer> collect(List<Integer> collection) {
		return collection::add;
	}

	@Test
	public void hot() throws Exception {
		var first = new ArrayList<Integer>();
		var second = new ArrayList<Integer>();
		
		Sinks.Many<Integer> sinks = Sinks.many().multicast().onBackpressureBuffer(2);
		sinks.tryEmitNext(1);
		sinks.tryEmitNext(2);
		sinks.asFlux().subscribe(collect(first));
		sinks.tryEmitNext(3);
		sinks.asFlux().subscribe(collect(second));
		sinks.tryEmitComplete();
		
		assertThat(first.size() > second.size()).isTrue();

	}

}
