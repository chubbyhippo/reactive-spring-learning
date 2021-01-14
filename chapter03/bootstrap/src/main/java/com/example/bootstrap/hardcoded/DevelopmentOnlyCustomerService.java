package com.example.bootstrap.hardcoded;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import com.example.bootstrap.BaseCustomerService;
import com.example.bootstrap.DataSourceUtils;

public class DevelopmentOnlyCustomerService extends BaseCustomerService {

	protected DevelopmentOnlyCustomerService() {
		super(buildDataSource());
	}

	private static DataSource buildDataSource() {
		DataSource dataSource = new EmbeddedDatabaseBuilder()
				.setType(EmbeddedDatabaseType.H2).build();
		return DataSourceUtils.initializedDbl(dataSource);
	}

}
