package com.example.webflux.filters;

import java.net.URI;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;

@Component
public class LowercaseWebFilter implements WebFilter {

	@Override
	public Mono<Void> filter(ServerWebExchange currentRequest,
			WebFilterChain chain) {

		var lowercaseUri = URI.create(
				currentRequest.getRequest().getURI().toString().toLowerCase());

		var outgoingExchange = currentRequest.mutate()
				.request(builder -> builder.uri(lowercaseUri)).build();

		return chain.filter(outgoingExchange);
	}

}
