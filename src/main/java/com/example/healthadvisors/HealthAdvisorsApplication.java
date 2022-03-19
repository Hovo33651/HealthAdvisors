package com.example.healthadvisors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class HealthAdvisorsApplication {

    public static void main(String[] args) {
        SpringApplication.run(HealthAdvisorsApplication.class, args);
    }

}
