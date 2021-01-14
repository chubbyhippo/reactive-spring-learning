package com.example.bootstrap.basicdi;

import javax.sql.DataSource;

import com.example.bootstrap.BaseCustomerService;

public class DataSourceCustomerService extends BaseCustomerService {
	
	public DataSourceCustomerService(DataSource ds) {
		super(ds);
	}

}
