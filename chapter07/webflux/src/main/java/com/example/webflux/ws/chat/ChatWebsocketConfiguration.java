package com.example.webflux.ws.chat;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SignalType;

@Configuration
public class ChatWebsocketConfiguration {

	ChatWebsocketConfiguration(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	private final Map<String, Connection> sessions = new ConcurrentHashMap<>();

	private final BlockingQueue<Message> messages = new LinkedBlockingQueue<>();

	private final ObjectMapper objectMapper;

	@Bean
	WebSocketHandler chatWsh() {

		var messagesToBroadcast = Flux.<Message>create(sink -> {
			var submit = Executors.newSingleThreadExecutor().submit(() -> {
				while (true) {
					try {
						sink.next(this.messages.take());
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
				}
			});
			sink.onCancel(() -> submit.cancel(true));
		})
				.share();

		return session -> {

			var sessionId = session.getId();

			this.sessions.put(sessionId, new Connection(sessionId, session));

			var in = session
					.receive()
					.map(WebSocketMessage::getPayloadAsText)
					.map(this::messageFromJson)
					.map(msg -> new Message(sessionId, msg.getText(),
							new Date()))
					.map(this.messages::offer)//
					.doFinally(st -> { // <7>
						if (st.equals(SignalType.ON_COMPLETE)) {//
							this.sessions.remove(sessionId);//
						}
					}); //

			var out = messagesToBroadcast // <8>
					.map(this::jsonFromMessage)//
					.map(session::textMessage);

			return session.send(out).and(in);
		};
	}

	@SneakyThrows
	private Message messageFromJson(String json) {
		return this.objectMapper.readValue(json, Message.class);
	}

	@SneakyThrows
	private String jsonFromMessage(Message msg) {
		return this.objectMapper.writeValueAsString(msg);
	}

	@Bean
	HandlerMapping chatHm() {
		return new SimpleUrlHandlerMapping() {
			{
				this.setUrlMap(Map.of("/ws/chat", chatWsh()));
				this.setOrder(2);
			}
		};
	}
}
