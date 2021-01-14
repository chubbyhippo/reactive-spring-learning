package com.example.bootstrap.templates;

import java.util.Collection;

import javax.sql.DataSource;

import org.springframework.transaction.support.TransactionTemplate;

import com.example.bootstrap.BaseCustomerService;
import com.example.bootstrap.Customer;

public class TransactionTemplateCustomerService extends BaseCustomerService {

	private final TransactionTemplate transactionTemplate;

	protected TransactionTemplateCustomerService(DataSource ds,
			TransactionTemplate tt) {
		super(ds);
		this.transactionTemplate = tt;
	}

	@Override
	public Collection<Customer> save(String... names) {
		return this.transactionTemplate.execute(s -> super.save(names));
	}

	@Override
	public Customer findById(Long id) {
		return this.transactionTemplate.execute(s -> super.findById(id));
	}

	@Override
	public Collection<Customer> findAll() {
		return this.transactionTemplate.execute(s -> super.findAll());
	}

}
