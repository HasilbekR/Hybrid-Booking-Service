package com.example.hybridbookingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
@SpringBootApplication
@EnableDiscoveryClient
public class HybridBookingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(HybridBookingServiceApplication.class, args);
    }

}
