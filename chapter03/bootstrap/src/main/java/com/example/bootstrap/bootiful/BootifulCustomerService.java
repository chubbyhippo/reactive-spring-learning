package com.example.bootstrap.bootiful;

import javax.sql.DataSource;

import org.springframework.stereotype.Service;

import com.example.bootstrap.enable.TransactionalCustomerService;

@Service
public class BootifulCustomerService extends TransactionalCustomerService {

	public BootifulCustomerService(DataSource ds) {
		super(ds);
	}

}
