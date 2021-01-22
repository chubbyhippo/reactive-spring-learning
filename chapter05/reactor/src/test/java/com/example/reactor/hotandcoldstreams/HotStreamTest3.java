package com.example.reactor.hotandcoldstreams;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class HotStreamTest3 {

	private List<Integer> one = new ArrayList<Integer>();
	private List<Integer> two = new ArrayList<Integer>();
	private List<Integer> three = new ArrayList<Integer>();

	private Consumer<Integer> subscribe(List<Integer> list) {
		return list::add;
	}

	@Test
	public void publish() throws Exception {
		Flux<Integer> pileOn = Flux.just(1, 2, 3).publish().autoConnect(3)
				.subscribeOn(Schedulers.immediate());
		
		pileOn.subscribe(subscribe(one));
		assertThat(one.size()).isEqualTo(0);
		
		pileOn.subscribe(subscribe(two));
		assertThat(two.size()).isEqualTo(0);

		pileOn.subscribe(subscribe(three));
		assertThat(three.size()).isEqualTo(3);
		assertThat(two.size()).isEqualTo(3);
		assertThat(three.size()).isEqualTo(3);

	}
}
