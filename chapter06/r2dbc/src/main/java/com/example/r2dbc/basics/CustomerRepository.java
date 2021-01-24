package com.example.r2dbc.basics;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;

import org.springframework.stereotype.Repository;

import com.example.r2dbc.Customer;
import com.example.r2dbc.SimpleCustomerRepository;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

@Repository
@RequiredArgsConstructor
public class CustomerRepository implements SimpleCustomerRepository {

	private final ConnectionManager connectionManager;

	private final BiFunction<Row, RowMetadata, Customer> mapper = (row,
			rowMatadata) -> new Customer(row.get("id", Integer.class),
					row.get("email", String.class));

	@Override
	public Mono<Customer> save(Customer customer) {
		return connectionManager.inConnection(
				conn -> Flux
						.from(conn.createStatement(
								"INSERT INTO customer(email) VALUES($1)")
								.bind("$1", customer.getEmail())
								.returnGeneratedValues("id").execute())
						.flatMap(r -> r.map((row, rowMetadata) -> {
							var id = row.get("id", Integer.class);
							return new Customer(id, customer.getEmail());
						})))
				.single().log();
	}

	@Override
	public Flux<Customer> findAll() {
		return connectionManager
				.inConnection(
						conn -> Flux
								.from(conn
										.createStatement(
												"select * from customer ")
										.execute())
								.flatMap(result -> result.map(mapper)));
	}

	@Override
	public Mono<Customer> update(Customer customer) {
		return connectionManager
				.inConnection(conn -> Flux.from(conn
						.createStatement(
								"update customer set email = $1 where id = $2")
						.bind("$1", customer.getEmail())
						.bind("$2", customer.getId()).execute()))
				.then(findById(customer.getId()));
	}

	@Override
	public Mono<Customer> findById(Integer id) {
		return connectionManager.inConnection(conn -> Flux.from(
				conn.createStatement("select * from customer where id = $1")
						.bind("$1", id)
						.execute()))
				.flatMap(result -> result.map(this.mapper)).single().log();
	}

	@Override
	public Mono<Void> deleteById(Integer id) {
		return connectionManager.inConnection(conn -> Flux
				.from(conn.createStatement("delete from customer where id = $1")
						.bind("$1", id)
						.execute()))
				.then();
	}

}
