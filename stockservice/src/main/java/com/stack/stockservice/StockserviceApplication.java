package com.stack.stockservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages={
        "com.stack.stockservice.controller"}
)
@EnableDiscoveryClient
public class StockserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(StockserviceApplication.class, args);
    }

}
