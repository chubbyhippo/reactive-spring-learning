package com.example.bootstrap.enable;

import java.util.Collection;

import javax.sql.DataSource;

import com.example.bootstrap.BaseCustomerService;
import com.example.bootstrap.Customer;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TransactionalCustomerService extends BaseCustomerService {

    public TransactionalCustomerService(DataSource ds) {
        super(ds);
    }

    @Override
    public Collection<Customer> findAll() {
        return super.findAll();
    }

    @Override
    public Customer findById(Long id) {
        return super.findById(id);
    }

    @Override
    public Collection<Customer> save(String... names) {
        return super.save(names);
    }

}
