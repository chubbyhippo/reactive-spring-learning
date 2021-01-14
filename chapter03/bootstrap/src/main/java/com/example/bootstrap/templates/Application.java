package com.example.bootstrap.templates;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import com.example.bootstrap.DataSourceUtils;
import com.example.bootstrap.Demo;

public class Application {
	public static void main(String[] args) {

		DataSource dataSource = new EmbeddedDatabaseBuilder()
				.setType(EmbeddedDatabaseType.H2).build();
		DataSource initializeDataSource = DataSourceUtils
				.initializedDbl(dataSource);
		PlatformTransactionManager dsTxManager = new DataSourceTransactionManager(
				initializeDataSource);
		TransactionTemplate tt = new TransactionTemplate(dsTxManager);

		TransactionTemplateCustomerService customerService = new TransactionTemplateCustomerService(
				initializeDataSource, tt);
		Demo.workWithCustomerService(Application.class, customerService);

	}
}
