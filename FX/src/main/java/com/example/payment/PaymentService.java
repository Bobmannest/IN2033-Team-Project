package com.example.payment;

import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    public void processPayment(Payment payment) {
        System.out.println("Data Received:" + "\n" +
                payment.getPaymentID() + "\n" +
                payment.getHolderID() + "\n" +
                payment.getAmount() + "\n" +
                payment.getPaymentDate() + "\n" +
                payment.getNotes()
        );
    }
}
