package com.traveler.realtimepopular;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.util.TimeZone;

@SpringBootApplication
@EnableDiscoveryClient
public class RealtimepopularApplication {

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
		SpringApplication.run(RealtimepopularApplication.class, args);
	}

}
