package com.traveler.logpipeline;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class LogpipelineApplication {

	public static void main(String[] args) {
		SpringApplication.run(LogpipelineApplication.class, args);
	}

}
