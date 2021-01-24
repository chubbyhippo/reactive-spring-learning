package com.example.r2dbc.basics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import lombok.RequiredArgsConstructor;

@SpringBootApplication
@EnableTransactionManagement
@RequiredArgsConstructor
public class BasicsApplication {

	public static void main(String args[]) {
		SpringApplication.run(BasicsApplication.class, args);
	}

}