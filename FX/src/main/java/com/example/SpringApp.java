package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class SpringApp {
    public static void main(String[] args) {
        SpringApplication.run(SpringApp.class, args);
        System.out.println("\n\u001B[32m [Spring App started successfully] \u001B[0m");
        System.out.println(" Test Email service with: curl -X POST \"http://localhost:8080/api/emails/sendTest?recipient_email=exampleEmail@gmail.com\"");
        System.out.println(" Test Payment service with PaymentTester.java");
        System.out.println(" Test Catalogue service with CatalogueTester.java");
    }
}