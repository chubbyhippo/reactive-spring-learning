package com.example.bootstrap;

import java.util.Collection;

public interface CustomerService {
	
	Collection<Customer> save(String... names);
	
	CustomerService findById(Long id);
	
	Collection<Customer>findAll(); 
}
