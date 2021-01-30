package com.example.webflux.ws.echo;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;

import com.example.webflux.WebfluxApplication;
import com.example.webflux.ws.WebsocketConfiguration;

import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {WebfluxApplication.class,
		WebsocketConfiguration.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EchoWebsocketConfigurationTest {

	@LocalServerPort
	private int port;

	@Test
	public void testNotificationsOnUpdates() {
		var socketClient = new ReactorNettyWebSocketClient();
		var max = 2;
		var values = new ArrayList<>();
		var uri = URI.create("ws://localhost:" + this.port + "/ws/echo");
		var execute = socketClient.execute(uri, session -> {
			var map = session.receive()
					.map(WebSocketMessage::getPayloadAsText)
					.map(str -> str + " reply")
					.doOnNext(values::add)
					.map(session::textMessage)
					.take(max);

			return session.send(map).then();
		});
		StepVerifier
				.create(execute)
				.expectComplete()
				.verify(Duration.ofSeconds(max + 2));

		assertThat(max).isEqualTo(values.size());
	}

}
