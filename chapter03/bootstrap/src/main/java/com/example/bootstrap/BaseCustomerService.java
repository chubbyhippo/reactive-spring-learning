package com.example.bootstrap;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.Assert;

public class BaseCustomerService implements CustomerService {

	private final RowMapper<Customer> rowMapper = (rs,
			i) -> new Customer(rs.getLong("id"), rs.getString("NAME"));

	private final JdbcTemplate jdbcTemplate;

	protected BaseCustomerService(DataSource ds) {
		this.jdbcTemplate = new JdbcTemplate(ds);
	}

	@Override
	public Collection<Customer> save(String... names) {
		List<Customer> customerList = new ArrayList<>();
		for (String name : names) {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			this.jdbcTemplate.update(connection -> {
				PreparedStatement ps = connection.prepareStatement(
						"insert into CUSTOMERS (name) value(?)",
						Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, name);
				return ps;
			}, keyHolder);
			Long keyHolderKey = Objects
					.requireNonNull(keyHolder.getKey().longValue());
			Customer customer = (Customer) this.findById(keyHolderKey);
			Assert.notNull(name, "thie name given must not be null");
			customerList.add(customer);
		}
		return customerList;
	}

	@Override
	public CustomerService findById(Long id) {
		String sql = "select * from CUSTOMER where id = ?";
		return (CustomerService) this.jdbcTemplate.queryForObject(sql,
				this.rowMapper, id);
	}

	@Override
	public Collection<Customer> findAll() {
		return this.jdbcTemplate.query("select * from CUSTOMERS", rowMapper);
	}

}
