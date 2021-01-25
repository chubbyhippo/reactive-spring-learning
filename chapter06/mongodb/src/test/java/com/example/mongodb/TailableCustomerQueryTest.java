package com.example.mongodb;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedDeque;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.mongodb.reactivestreams.client.MongoCollection;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@Log4j2
@DataMongoTest
@ExtendWith(SpringExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
public class TailableCustomerQueryTest {

	@Autowired
	private ReactiveMongoTemplate operations;

	@Autowired
	private CustomerRepository repository;

	@BeforeAll
	public void beforeAll() {

		CollectionOptions capped = CollectionOptions.empty().size(1024 * 1024)
				.maxDocuments(100).capped();

		Mono<MongoCollection<org.bson.Document>> recreateCollection = operations
				.collectionExists(Order.class)
				.flatMap(exists -> exists
						? operations.dropCollection(Customer.class)
						: Mono.just(exists))
				.then(operations.createCollection(Customer.class, capped));

		StepVerifier.create(recreateCollection).expectNextCount(1)
				.verifyComplete();
	}

	private Mono<Customer> write() {
		return repository.save(new Customer(UUID.randomUUID().toString(), "1"));
	}

	@Test
	public void tail() throws InterruptedException {

		Queue<Customer> people = new ConcurrentLinkedDeque<>();

		StepVerifier
				.create(write().then(write()))
				.expectNextCount(1)
				.verifyComplete();

		repository.findByName("1")
				.doOnNext(people::add)
				.doOnComplete(() -> log.info("complete"))
				.doOnTerminate(() -> log.info("terminated"))
				.subscribe();

		assertThat(people).hasSize(2);

		StepVerifier.create(write().then(write()))
				.expectNextCount(1)
				.verifyComplete();
		
		Thread.sleep(1000);
		assertThat(people).hasSize(4);

	}
}
