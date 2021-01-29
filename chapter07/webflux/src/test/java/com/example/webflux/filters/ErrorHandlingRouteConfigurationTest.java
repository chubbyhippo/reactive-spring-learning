package com.example.webflux.filters;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

@ExtendWith(SpringExtension.class)
@WebFluxTest({ErrorHandlingRouteConfiguration.class})
public class ErrorHandlingRouteConfigurationTest {

	@Autowired
	private WebTestClient client;

	@Test
	public void valid() {
		client.get().uri("/products/4").exchange().expectStatus().isOk()
				.expectBody()
				.jsonPath(".id").isEqualTo("4");
	}

	@Test
	public void invalid() {
		client.get().uri("/products/1").exchange().expectStatus().isNotFound();
	}
}
