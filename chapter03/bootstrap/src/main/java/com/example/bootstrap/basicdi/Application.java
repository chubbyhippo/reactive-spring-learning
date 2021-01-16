package com.example.bootstrap.basicdi;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import com.example.bootstrap.CustomerService;
import com.example.bootstrap.DataSourceUtils;
import com.example.bootstrap.Demo;

public class Application {

    public static void main(String[] args) {

        DataSource dataSource = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2).build();

        DataSource initializedDataSource = DataSourceUtils
                .initializedDbl(dataSource);
        CustomerService cs = new DataSourceCustomerService(
                initializedDataSource);
        Demo.workWithCustomerService(Application.class, cs);
    }

}
