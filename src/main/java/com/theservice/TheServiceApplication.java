package com.theservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@PropertySource(value = { "github.properties" })
public class TheServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TheServiceApplication.class, args);
    }
}
