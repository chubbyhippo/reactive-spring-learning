package com.example.webflux.filters;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProductNotFoundException extends RuntimeException {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final String productId;

}
