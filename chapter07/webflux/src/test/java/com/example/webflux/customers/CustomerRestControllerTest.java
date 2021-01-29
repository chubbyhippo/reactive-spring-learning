package com.example.webflux.customers;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@WebFluxTest
@ExtendWith(SpringExtension.class)
public class CustomerRestControllerTest extends AbstractRestBaseClass {

	@Override
	String rootUrl() {
		return "/rc";
	}
	

}
