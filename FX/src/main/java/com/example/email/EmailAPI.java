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
    public String sendPurchaseEmail(
            @RequestParam String recipientName,
            @RequestParam String recipientEmail,
            @RequestParam String recipientAddress,
            @RequestParam String trackId)
    {
        emailService.sendPurchaseEmail(recipientName, recipientEmail, recipientAddress, trackId);
        return "Purchase Email sent to " + recipientEmail;
    }

    @PostMapping("/sendTest")
    public String sendEmail(@RequestParam String recipient_email) {
        emailService.sendTestEmail(recipient_email);
        return "Test Email sent to " + recipient_email;
    }

    @PostMapping("/sendRegistration")
    public String sendRegistrationEmail(@RequestParam String recipient_email,
                                        @RequestParam String generated_password) {
        emailService.sendRegistrationEmail(recipient_email, generated_password);
        return "Registration email sent to " + recipient_email;
    }

    @PostMapping("/sendPassReset")
    public String sendPasswordResetEmail(@RequestParam String recipient_email,
                                        @RequestParam String tempPass) {
        emailService.sendPasswordResetEmail(recipient_email, tempPass);
        return "Registration email sent to " + recipient_email;
    }
}
