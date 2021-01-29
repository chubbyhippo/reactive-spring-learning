package com.example.webflux.sse;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.test.StepVerifier;

@WebFluxTest({SseConfiguration.class})
@ExtendWith(SpringExtension.class)
public class SseConfigurationTest {

	@Autowired
	private WebTestClient client;

	@Test
	public void sse() {

		StepVerifier
				.create(
						this.client
								.get()
								.uri("/sse/2")
								.exchange()
								.expectStatus().isOk()
								.expectHeader()
								.contentTypeCompatibleWith(
										MediaType.TEXT_EVENT_STREAM)
								.returnResult(String.class)
								.getResponseBody())
				.expectNext("# 1")
				.expectNext("# 2")
				.verifyComplete();

	}
}