package com.example.r2dbc;

import org.springframework.data.repository.NoRepositoryBean;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@NoRepositoryBean
public interface SimpleCustomerRepository {

	Mono<Customer> save(Customer customer);
	Flux<Customer> findAll();
	Mono<Customer> update(Customer customer);
	Mono<Customer> findById(Integer id);
	Mono<Void> deleteById(Integer id);

}
