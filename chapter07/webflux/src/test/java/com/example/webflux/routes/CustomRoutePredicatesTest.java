package com.example.webflux.routes;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.hamcrest.Matchers.equalToIgnoringCase;

@ExtendWith(SpringExtension.class)
@WebFluxTest({CustomRoutePredicates.class})
class CustomRoutePredicatesTest {

	@Autowired
	private WebTestClient webTestClient;

	@Test
	public void caseInsensitiveRequestMatching() throws Exception {

		var uppercaseUri = "/GREETINGS/world";
		var lowercaseUri = "/greetings/World";
		var expectedString = "Hello, World!";

		webTestClient.get().uri(uppercaseUri).exchange()
				.expectBody(String.class)
				.value(equalToIgnoringCase(expectedString));

		webTestClient.get().uri(lowercaseUri).exchange()
				.expectBody(String.class)
				.value(equalToIgnoringCase(expectedString));
	}

}
