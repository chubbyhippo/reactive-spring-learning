package com.example.webflux.customers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public abstract class AbstractRestBaseClass {

	abstract String rootUrl();

	@Autowired
	private WebTestClient client;

	@MockBean
	private CustomerRepository customerRepository;

	private final Collection<Customer> results = Arrays.asList(
			new Customer("1", "A"),
			new Customer("2", "B"), new Customer("3", "C"),
			new Customer("4", "D"));

	private final AtomicReference<Customer> saved = new AtomicReference<>();

	@Test
	public void all() throws Exception {
		var iterable = Flux.fromIterable(this.results);
		Mockito.when(customerRepository.findAll())
				.thenReturn(iterable);

		var customerFluxExchangeResult = client.get()
				.uri(rootUrl() + "/customers")
				.exchange()
				.expectStatus().isOk()
				.expectHeader()
				.contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
				.returnResult(Customer.class);

		var responseBody = customerFluxExchangeResult.getResponseBody();

		StepVerifier.create(responseBody)
				.expectNextCount(results.size()).verifyComplete();
	}

	@Test
	public void byId() throws Exception {
		Mockito.when(customerRepository.findById("1"))
				.thenAnswer(invocation -> Mono.just(new Customer("1", "A")));

		var getCustomerByIdResult = client.get()
				.uri(rootUrl() + "/customers/1")
				.exchange()
				.expectStatus().isOk()
				.expectHeader()
				.contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
				.returnResult(Customer.class);

		var responseBody = getCustomerByIdResult.getResponseBody();

		StepVerifier.create(responseBody)
				.expectNextMatches(
						customer -> customer.getName().equalsIgnoreCase("A"))
				.verifyComplete();
	}
	@Test
	public void create() throws Exception {
		Mockito.when(customerRepository.save(Mockito.any()))
				.then(invocation -> {
					Customer customer = Customer.class
							.cast(invocation.getArguments()[0]);
					String uid = UUID.randomUUID().toString();
					saved.set(new Customer(uid, customer.getName()));
					return Mono.just(saved.get());
				});

		var krusty = "Krusty";

		client.post()
				.uri(rootUrl() + "/customers")
				.contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromValue(new Customer(null, krusty)))
				.exchange()
				.expectHeader()
				.exists(org.springframework.http.HttpHeaders.LOCATION)
				.expectStatus().isCreated();

		assertThat(saved.get().getName()).isEqualToIgnoringCase(krusty);
	}

}
