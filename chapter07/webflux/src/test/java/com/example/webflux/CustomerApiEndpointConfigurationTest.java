package com.example.webflux;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.webflux.customers.CustomerApiEndpointConfiguration;
import com.example.webflux.customers.CustomerHandler;

@WebFluxTest
@ExtendWith(SpringExtension.class)
@Import({CustomerApiEndpointConfiguration.class, CustomerHandler.class})
public class CustomerApiEndpointConfigurationTest extends AbstractRestBaseClass {

	@Override
	String rootUrl() {
		return "/fn";
	}

}
