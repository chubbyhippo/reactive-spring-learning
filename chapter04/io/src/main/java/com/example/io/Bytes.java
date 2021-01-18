package com.example.io;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Bytes {
	private final byte[] bytes;

	private final int length;

	public static Bytes from(byte[] bytes, int len) {
		return new Bytes(bytes, len);
	}
}
