package com.example.webflux.ws.echo;

import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;

import com.example.webflux.utils.IntervalMessageProducer;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SignalType;

@Configuration
@Log4j2
public class EchoWebsocketConfiguration {

	@Bean
	WebSocketHandler echoWsh() {
		return session -> {

			Flux<WebSocketMessage> out = IntervalMessageProducer
					.produce()
					.doOnNext(log::info)
					.map(session::textMessage)
					.doFinally(
							signalType -> log.info(
									"outbound connection: " + signalType));

			Flux<String> in = session
					.receive()
					.map(WebSocketMessage::getPayloadAsText)
					.doFinally(signalType -> {
						log.info("inbound connection: " + signalType);
						if (signalType.equals(SignalType.ON_COMPLETE)) {
							session.close().subscribe();
						}
					}).doOnNext(log::info);
			return session.send(out).and(in);
		};
	}

	@Bean
	HandlerMapping echoHm() {
		return new SimpleUrlHandlerMapping(Map.of("/ws/echo", echoWsh()), 10);
	}
}
