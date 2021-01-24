package com.example.r2dbc;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
	
	@Id
	private Integer id;
	private String email;
	
	Customer(String email) {
		this.email = email;
	}
	
}
