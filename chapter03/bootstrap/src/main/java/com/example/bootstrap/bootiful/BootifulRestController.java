package com.example.bootstrap.bootiful;

import java.util.Collection;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bootstrap.Customer;
import com.example.bootstrap.CustomerService;

@RestController
public class BootifulRestController {

	private final CustomerService customerService;

	public BootifulRestController(CustomerService customerService) {
		this.customerService = customerService;
	}

	@GetMapping("/customers")
	Collection<Customer> get() {
		return this.customerService.findAll();
	}
}
