package com.example.r2dbc.basics;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.reactivestreams.Publisher;
import org.springframework.r2dbc.connection.ConnectionFactoryUtils;
import org.springframework.stereotype.Component;

import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ConnectionManager {

	private final ConnectionFactory connectionFactory;
	@RequiredArgsConstructor
	static class ConnectionCloseHolder extends AtomicBoolean {

		private static final long serialVersionUID = -8994138383301201380L;

		final Connection connection;

		final Function<Connection, Publisher<Void>> closeFunction;

		Mono<Void> close() {

			return Mono.defer(() -> {

				if (compareAndSet(false, true)) {
					return Mono.from(this.closeFunction.apply(this.connection));
				}

				return Mono.empty();
			});
		}

	}

	private Publisher<Void> closeConnection(Connection connection) {
		return ConnectionFactoryUtils
				.currentConnectionFactory(this.connectionFactory)
				.then()
				.onErrorResume(Exception.class,
						e -> Mono.from(connection.close()));
	}
	public <T> Flux<T> inConnection(Function<Connection, Flux<T>> action) {

		Function<ConnectionCloseHolder, Publisher<?>> closeFunction = ConnectionCloseHolder::close;

		BiFunction<ConnectionCloseHolder, Throwable, Publisher<?>> closeBiFunction = (
				connectionCloseHolder,
				throwable) -> closeFunction.apply(connectionCloseHolder);

		return Flux.usingWhen(connection(),
				holder -> action.apply(holder.connection),
				closeFunction, closeBiFunction, closeFunction);
	}

	private Mono<ConnectionCloseHolder> connection() {
		return ConnectionFactoryUtils//
				.getConnection(connectionFactory)//
				.map(c -> new ConnectionCloseHolder(c, this::closeConnection));
	}

}
