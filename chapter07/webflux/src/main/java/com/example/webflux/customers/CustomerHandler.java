package com.example.webflux.customers;

import static org.springframework.web.reactive.function.server.ServerResponse.created;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import java.net.URI;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CustomerHandler {

	private final CustomerRepository repository;

	Mono<ServerResponse> handleFindAll(ServerRequest request) {
		var all = this.repository.findAll();
		return ok().body(all, Customer.class);
	}
	
	Mono<ServerResponse> handleFindCustomerById(ServerRequest request) {
		var id = request.pathVariable("id");
		var byId = this.repository.findById(id);
		return ok().body(byId, Customer.class);
	}
	
	Mono<ServerResponse> handleCreateCustomer(ServerRequest request) {
		return request.bodyToMono(Customer.class)
				.flatMap(repository::save)
				.flatMap(saved -> created(URI.create("/fn/customers/" + saved.getId()))
						.build());
	}

}
