package com.stack.authenticationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

//(scanBasePackages={"com.stack.authenticationservice.controller","com.stack.authenticationservice.service","com.stack.authenticationservice.config"})
@EnableDiscoveryClient
@SpringBootApplication
public class AuthenticationserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthenticationserviceApplication.class, args);
	}

}
