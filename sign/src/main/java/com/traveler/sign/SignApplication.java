package com.traveler.sign;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SignApplication {

	public static void main(String[] args) {
		SpringApplication.run(SignApplication.class, args);
	}

}
