package com.example.healthadvisors;

import com.example.healthadvisors.repository.UserRepository;
import  org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class HealthAdvisorsApplication {


    public static void main(String[] args) {
        SpringApplication.run(HealthAdvisorsApplication.class, args);
    }

}
