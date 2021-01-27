package com.example.webflux.routes;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

@ExtendWith(SpringExtension.class)
@WebFluxTest({SimpleFunctionalEndpointConfiguration.class,
		GreetingsHandlerFunction.class
})
public class SimpleFunctionalEndpointConfigurationTest {

	@Autowired
	private WebTestClient webTestClient;
	
	private void doTest(String path, String result) {
		webTestClient.get().uri(path)
		.exchange()
		.expectBody(String.class).value(str -> assertThat(result).isEqualTo(str));
	}
	
	@Test
	public void sup() {
		this.doTest("/sup", "Hodor!");
	}
	
	@Test
	public void hello() {
		this.doTest("/hello/world", "Hello world!");
	}
	
	@Test
	public void hodor() {
		this.doTest("/hodor", "Hodor!");
	}

}
