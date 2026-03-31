package com.example.email;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/emails")
public class EmailAPI {

    private final EmailService emailService;

    public EmailAPI(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/sendPurchase")
    public String sendPurchaseEmail(@RequestParam String recipient_email) {
        emailService.sendPurchaseEmail(recipient_email);
        return "Purchase Email sent to " + recipient_email;
    }

    @PostMapping("/sendTest")
    public String sendEmail(@RequestParam String recipient_email) {
        emailService.sendTestEmail(recipient_email);
        return "Test Email sent to " + recipient_email;
    }
}
