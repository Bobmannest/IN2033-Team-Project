package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringApp {
    public static void main(String[] args) {
        SpringApplication.run(SpringApp.class, args);
        System.out.println("\n[Spring App started successfully]");
        System.out.println("\nTest Email service with: curl -X POST \"http://localhost:8080/api/emails/send?recipient_email=exampleEmail@gmail.com\"\n");
    }
}
