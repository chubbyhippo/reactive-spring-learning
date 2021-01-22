package com.example.reactor.hotandcoldstreams;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.FluxSink;

@SuppressWarnings("deprecation")
public class HotStreamTest1 {

    Consumer<Integer> collect(List<Integer> collection) {
        return collection::add;
    }

    @Test
    public void hot() throws Exception {
        var first = new ArrayList<Integer>();
        var second = new ArrayList<Integer>();

		EmitterProcessor<Integer> emitter = EmitterProcessor.create(2);
        FluxSink<Integer> sink = emitter.sink();

        emitter.subscribe(collect(first));
        sink.next(1);
        sink.next(2);

        emitter.subscribe(collect(second));
        sink.next(3);
        sink.complete();

        assertThat(first.size() > second.size()).isTrue();


    }

}
