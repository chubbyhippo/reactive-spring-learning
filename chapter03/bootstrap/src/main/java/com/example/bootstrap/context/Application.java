package com.example.bootstrap.context;

import javax.sql.DataSource;

import org.apache.catalina.core.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import com.example.bootstrap.CustomerService;
import com.example.bootstrap.Demo;
import com.example.bootstrap.templates.TransactionTemplateCustomerService;

@Configuration
@Import(DataSourceConfiguration.class)
public class Application {

	@Bean
	PlatformTransactionManager transactionManager(DataSource ds) {
		return new DataSourceTransactionManager(ds);
	}

	@Bean
	TransactionTemplateCustomerService customerService(DataSource ds,
			TransactionTemplate tt) {
		return new TransactionTemplateCustomerService(ds, tt);
	}

	@Bean
	TransactionTemplate transactionTemplate(PlatformTransactionManager tm) {
		return new TransactionTemplate(tm);
	}

	public static void main(String[] args) {

		ApplicationContext ac = SpringUtils.run(Application.class, "prod");

		CustomerService cs = ac.getBean(CustomerService.class);
		Demo.workWithCustomerService(Application.class, cs);
	}
}
