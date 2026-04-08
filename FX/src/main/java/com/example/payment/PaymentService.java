package com.example.payment;

import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    public void printPayment(Payment payment) {
        System.out.println("Data Received:" + "\n" +
                payment.getFirstDigits() + "\n" +
                payment.getLastDigits() + "\n" +
                payment.getExpiryDate() + "\n" +
                payment.getCardType()
        );
    }

    public void recordPayment() {

    }

    public void processPayment() {

    }
}
