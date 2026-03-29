package com.example.payment;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/accept")
    public ResponseEntity<String> acceptPayment(@RequestBody Payment payment) {
        paymentService.processPayment(payment);
        return ResponseEntity.ok("Payment Accepted");
    }
}
