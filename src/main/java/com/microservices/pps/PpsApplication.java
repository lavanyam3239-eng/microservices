package com.microservices.pps;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class PpsApplication {

    public static void main(String[] args) {
        SpringApplication.run(PpsApplication.class, args);
    }
}