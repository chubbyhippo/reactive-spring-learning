package com.example.webflux.customers;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CustomerViewController {

	private final CustomerRepository repository;

	@GetMapping("/c/customers.php")
	String customerView(Model model) {
		var modelMap = Map.of("customers", repository.findAll(),
				"type", "@Controller");
		model.addAllAttributes(modelMap);
		return "customer";
	}

}
