package com.example.bootstrap;

import java.sql.Driver;

import javax.sql.DataSource;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

@Configuration
public class DataSourceConfiguration {

	@Configuration
	@Profile("prod")
	@PropertySource("application-prod.properties")
	public static class ProductionConfiguration {

		@Bean
		DataSource productionDataSource(
				@Value("${spring.datasource.url}") String url,
				@Value("${spring.datasource.username}") String username,

				@Value("${spring.datasource.password}") String password,
				@Value("${spring.datasource.driver-class-name}") Class<Driver> driverClass) {
			DriverManagerDataSource dataSource = new DriverManagerDataSource(
					url, username, password);

			dataSource.setDriverClassName(driverClass.getName());
			return dataSource;
		}
	}

	@Configuration
	@Profile("default")
	@PropertySource("application-default.properties")
	public static class DevelopmentConfiguration {

		@Bean
		DataSource developmentDataSource() {
			return new EmbeddedDatabaseBuilder()
					.setType(EmbeddedDatabaseType.H2).build();
		}

		@Bean
		DataSourcePostProcessor dataSourcePostProcessor() {
			return new DataSourcePostProcessor();
		}

		private static class DataSourcePostProcessor
				implements
					BeanPostProcessor {

			@Override
			public Object postProcessAfterInitialization(Object bean,
					String beanName) throws BeansException {
				if (bean instanceof DataSource) {
					DataSourceUtils.initializedDbl(DataSource.class.cast(bean));
				}
				return bean;
			}
		}
	}
}
