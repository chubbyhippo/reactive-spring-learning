package com.example.bootstrap.scan;

import javax.sql.DataSource;

import com.example.bootstrap.templates.TransactionTemplateCustomerService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

@Service
public class DiscoveredCustomerService extends TransactionTemplateCustomerService {

    public DiscoveredCustomerService(DataSource ds, TransactionTemplate tt) {
        super(ds, tt);
    }

}
