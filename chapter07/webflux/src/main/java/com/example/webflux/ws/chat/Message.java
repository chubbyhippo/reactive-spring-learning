package com.example.webflux.ws.chat;

import java.util.Date;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Message {
	
	private final String clientId;
	
	private final String text;
	
	private final Date when;

}
