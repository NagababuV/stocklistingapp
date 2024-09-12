package com.stack.userprofileservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages={"com.stack.userprofileservice.controller","com.stack.userprofileservice.service"})
@EnableDiscoveryClient
public class UserprofileserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserprofileserviceApplication.class, args);
	}

}
