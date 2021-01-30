package com.example.webflux.ws.chat;

import org.springframework.web.reactive.socket.WebSocketSession;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Connection {
	
	private final String id;
	
	private final WebSocketSession session;

}
