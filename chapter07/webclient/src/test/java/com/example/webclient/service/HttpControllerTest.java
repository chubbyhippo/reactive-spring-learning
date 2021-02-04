package com.example.webclient.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.webclient.client.ClientProperties;
import com.example.webclient.client.DefaultClient;
import com.example.webclient.client.DefaultConfiguration;
import com.example.webclient.client.Greeting;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest(classes = { DefaultConfiguration.class, ClientProperties.class,
		HttpServiceApplication.class }, 
		webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, 
		properties = { "spring.profiles.active=client", "server.port=8080",
				"spring.main.web-application-type=reactive" })
@ExtendWith(SpringExtension.class)
public class HttpControllerTest {

	@Autowired
	private DefaultClient defaultClient;

	@Test
	public void greetSingle() {
		Mono<Greeting> helloMono = this.defaultClient.getSingle("Madhura");
		StepVerifier.create(helloMono)
				.expectNextMatches(g -> g.getMessage().contains("Hello Madhura"))
				.verifyComplete();
	}

	@Test
	public void greetMany() {
		Flux<Greeting> helloFlux = this.defaultClient.getMany("Stephane").take(2);
		String msg = "Hello Stephane";
		StepVerifier.create(helloFlux)
				.expectNextMatches(g -> g.getMessage().contains(msg))
				.expectNextMatches(g -> g.getMessage().contains(msg)).verifyComplete();
	}

}