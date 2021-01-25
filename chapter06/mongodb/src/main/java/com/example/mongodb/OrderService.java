package com.example.mongodb;

import java.util.function.Function;

import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.util.Assert;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final ReactiveMongoTemplate template;

	private final TransactionalOperator operator;

	private Flux<Order> buildOrderFlux(Function<Order, Mono<Order>> callback,
			String[] productIds) {
		return Flux.just(productIds)
				.map(pid -> {
					Assert.notNull(pid, "the product ID shouldn't be null");
					return pid;
				})
				.map(x -> new Order(null, x))
				.flatMap(callback);
	}

	public Flux<Order> createOrder(String... productIds) {
		return this.operator.execute(
				status -> buildOrderFlux(template::insert, productIds));
	}

}
