package com.example.hybridbookingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class HybridBookingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(HybridBookingServiceApplication.class, args);
    }

}
