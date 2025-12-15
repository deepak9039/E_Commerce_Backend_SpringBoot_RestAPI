package com.store.e_commerce_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ECommerceAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(ECommerceAppApplication.class, args);
		System.out.println("E-Commerce Application is running... Port 1234");
	}

}
