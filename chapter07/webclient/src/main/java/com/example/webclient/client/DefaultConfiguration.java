package com.example.webclient.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class DefaultConfiguration {

	@Bean
	DefaultClient defaultClient(WebClient.Builder builder,
			ClientProperties properties) {
		var root = properties.getHttp().getRootUrl();
		return new DefaultClient(builder.baseUrl(root).build());// <1>
	}

}
