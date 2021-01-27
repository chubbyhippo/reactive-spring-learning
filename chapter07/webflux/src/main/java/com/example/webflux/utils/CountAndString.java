package com.example.webflux.utils;

import lombok.Data;

@Data
public class CountAndString {
	
	private final String message;
	
	private final long count;
	
	public CountAndString(long c) {
		count = c;
		message = "# " + this.count;
	}

}
