package com.example.webflux.customers;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/rc/customers")
@RequiredArgsConstructor
public class CustomerRestController {

	private final CustomerRepository repository;

	@GetMapping("/{id}")
	Mono<Customer> byId(@PathVariable("id") String id) {
		return repository.findById(id);
	}

	@GetMapping
	Flux<Customer> all() {
		return repository.findAll();
	}

	@PostMapping
	Mono<ResponseEntity<?>> create(@RequestBody Customer customer) {
		return repository.save(customer)
				.map(customerEntity -> ResponseEntity
						.created(URI.create(
								"/rc/customers/" + customerEntity.getId()))
						.build());
	}

}
