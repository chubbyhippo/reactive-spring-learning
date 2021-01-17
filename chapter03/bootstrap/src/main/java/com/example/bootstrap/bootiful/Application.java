package com.example.bootstrap.bootiful;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.example.bootstrap.CustomerService;
import com.example.bootstrap.Demo;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		System.setProperty("spring.profiles.active", "dev");
		SpringApplication.run(Application.class, args);
	}

}

@Profile("dev")
@Component
class DemoListener {

	private final CustomerService customerService;

	public DemoListener(CustomerService customerService) {
		this.customerService = customerService;
	}

	@EventListener(ApplicationReadyEvent.class)
	public void exercise() {
		Demo.workWithCustomerService(getClass(), customerService);
	}
}
