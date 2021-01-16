package com.example.bootstrap.scan;

import javax.sql.DataSource;

import com.example.bootstrap.CustomerService;
import com.example.bootstrap.DataSourceConfiguration;
import com.example.bootstrap.Demo;
import com.example.bootstrap.SpringUtils;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
@ComponentScan
@Import(DataSourceConfiguration.class)
public class Application {

    @Bean
    PlatformTransactionManager transactionManager(DataSource ds) {
        return new DataSourceTransactionManager(ds);
    }

    @Bean
    TransactionTemplate transactionTemplate(PlatformTransactionManager tm) {
        return new TransactionTemplate(tm);
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringUtils.run(Application.class, "prod");
        CustomerService customerService = applicationContext.getBean(CustomerService.class);
        Demo.workWithCustomerService(Application.class, customerService);
    }

}
