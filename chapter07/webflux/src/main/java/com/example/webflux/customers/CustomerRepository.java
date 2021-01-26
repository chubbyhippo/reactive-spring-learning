package com.example.webflux.customers;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class CustomerRepository {

	private final Map<String, Customer> data = new ConcurrentHashMap<>();

	public Mono<Customer> findById(String id) {
		return Mono.just(data.get(id));
	}

	public Mono<Customer> save(Customer customer) {
		var uuid = UUID.randomUUID().toString();
		data.put(uuid, new Customer(uuid, customer.getName()));
		return Mono.just(data.get(uuid));
	}

	public Flux<Customer> findAll() {
		return Flux.fromIterable(this.data.values());
	}

}
