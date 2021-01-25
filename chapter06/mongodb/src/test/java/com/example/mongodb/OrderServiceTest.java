package com.example.mongodb;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
@DataMongoTest
@Import({
		TransactionConfiguration.class,
		OrderService.class
})
@TestInstance(Lifecycle.PER_CLASS)
public class OrderServiceTest {

	@Autowired
	private OrderRepository repository;

	@Autowired
	private OrderService service;

	@Autowired
	private ReactiveMongoTemplate template;

	@BeforeAll
	public void configureCollectionsBeforeTests() throws Exception {
		Mono<Boolean> createIfMissing = template.collectionExists(Order.class)
				.filter(x -> !x)
				.flatMap(exists -> template.createCollection(Order.class))
				.thenReturn(true);

		StepVerifier.create(createIfMissing)
				.expectNextCount(1)
				.verifyComplete();

	}

	private void runTransactionalTest(Flux<Order> orderInTx) {
		Publisher<Order> orders = this.repository
				.deleteAll()
				.thenMany(orderInTx)
				.thenMany(this.repository.findAll());

		StepVerifier
				.create(orders)
				.expectNextCount(0)
				.verifyError();

		StepVerifier
				.create(this.repository.findAll())
				.expectNextCount(0)
				.verifyComplete();
	}

	@Test
	public void transactionalOperatorRollback() {
		runTransactionalTest(service.createOrder("1", "2", null));
	}
}
