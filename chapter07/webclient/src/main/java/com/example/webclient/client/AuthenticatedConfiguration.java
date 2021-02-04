package com.example.webclient.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
class AuthenticatedConfiguration {

	@Bean
	AuthenticatedClient authenticatedClient(WebClient.Builder builder,
			ClientProperties clientProperties) {

		var httpProperties = clientProperties.getHttp();
		var basicAuthProperties = clientProperties.getHttp().getBasic();

		var filterFunction = ExchangeFilterFunctions.basicAuthentication(
				basicAuthProperties.getUsername(),
				basicAuthProperties.getPassword());

		WebClient client = builder
				.baseUrl(httpProperties.getRootUrl())
				.filters(filters -> filters.add(filterFunction))
				.build();
		return new AuthenticatedClient(client);
	}

}