package com.example.bootstrap.hardcoded;

import com.example.bootstrap.Demo;

public class Application {
	public static void main(String[] args) {
		DevelopmentOnlyCustomerService customerService = new DevelopmentOnlyCustomerService();
		Demo.workWithCustomerService(Application.class, customerService);
	}
}
