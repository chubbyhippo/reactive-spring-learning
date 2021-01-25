package com.example.mongodb;

import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.ReactiveMongoTransactionManager;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.reactive.TransactionalOperator;

@EnableTransactionManagement
public class TransactionConfiguration {

	@Bean
	TransactionalOperator transactionalOperator(
			ReactiveTransactionManager txm) {
		return TransactionalOperator.create(txm);
	}

	@Bean
	ReactiveTransactionManager reactiveTransactionManager(
			ReactiveMongoDatabaseFactory rdf) {
		return new ReactiveMongoTransactionManager(rdf);
	}

}
