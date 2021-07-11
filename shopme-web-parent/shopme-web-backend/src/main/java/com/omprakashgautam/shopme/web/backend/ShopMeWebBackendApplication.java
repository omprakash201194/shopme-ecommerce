package com.omprakashgautam.shopme.web.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.omprakashgautam.shopme"})
public class ShopMeWebBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopMeWebBackendApplication.class, args);
	}

}
