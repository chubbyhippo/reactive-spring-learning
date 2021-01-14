package com.example.bootstrap;

import javax.sql.DataSource;

import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

public abstract class DataSourceUtils {

	public static DataSource initializedDbl(DataSource dataSource) {
		ResourceDatabasePopulator populator = new ResourceDatabasePopulator(
				new ClassPathResource("/schema.sql"));
		DatabasePopulatorUtils.execute(populator, dataSource);
		return dataSource;
	}

}
