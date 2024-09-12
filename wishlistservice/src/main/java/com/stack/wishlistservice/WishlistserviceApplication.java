package com.stack.wishlistservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(scanBasePackages={"com.stack.wishlistservice.controller","com.stack.wishlistservice.service"})
@EnableDiscoveryClient
public class WishlistserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WishlistserviceApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
