package com.example.mongodb;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;
import java.util.function.Predicate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@DataMongoTest
@ExtendWith(SpringExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
public class OrderRepositoryTest {

	@Autowired
	private OrderRepository orderRepository;

	private final Collection<Order> orders = Arrays.asList(
			new Order(UUID.randomUUID().toString(), "1"),
			new Order(UUID.randomUUID().toString(), "2"),
			new Order(UUID.randomUUID().toString(), "2"));

	private final Predicate<Order> predicate = order -> this.orders.stream()
			.filter(candidateOrder -> candidateOrder.getId()
					.equalsIgnoreCase(order.getId()))
			.anyMatch(candidateOrder -> candidateOrder.getProductId()
					.equalsIgnoreCase(order.getProductId()));

	@BeforeAll
	public void beforeAll() {
		Flux<Order> saveAll = this.orderRepository.deleteAll()
				.thenMany(this.orderRepository.saveAll(this.orders));

		StepVerifier
				.create(saveAll)
				.expectNextMatches(predicate)
				.expectNextMatches(predicate)
				.expectNextMatches(predicate)
				.verifyComplete();
	}

	@Test
	public void findAll() {
		StepVerifier
				.create(this.orderRepository.findAll())
				.expectNextMatches(predicate)
				.expectNextMatches(predicate)
				.expectNextMatches(predicate)
				.verifyComplete();
	}

	@Test
	public void findByProductId() {
		StepVerifier
				.create(this.orderRepository.findByProductId("2"))
				.expectNextCount(2)
				.verifyComplete();
	}

}
