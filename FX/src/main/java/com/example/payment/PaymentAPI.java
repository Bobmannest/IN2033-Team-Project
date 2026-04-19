package com.example.payment;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentAPI {

    private final PaymentService paymentService;

    public PaymentAPI(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    //Accepts Payment from other subsystems
    @PostMapping("/accept")
    public ResponseEntity<String> acceptPayment(@RequestBody Payment payment) {
        paymentService.printPayment(payment);
        System.out.println("Payment Accepted");
        return ResponseEntity.ok("Payment Accepted");
    }

    //Processes PaymentDetails sent by other subsystems
    @PostMapping("/process")
    public ResponseEntity<String> processPayment(@RequestBody PaymentDetails paymentDetails) {
        boolean valid = paymentService.processPayment(paymentDetails);
        System.out.println("Payment Processed");
        if (valid) {
            return ResponseEntity.ok("Payment Details Accepted");
        } else {
            return ResponseEntity.badRequest().body("Invalid Payment Details");
        }
    }
}
