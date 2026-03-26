package com.example.email;


import org.springframework.web.bind.annotation.*;

import static com.example.email.EmailService.SendEmail;

@RestController
@RequestMapping("/api/emails")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send")
    public String sendEmail(@RequestParam String recipient_email) {
        SendEmail(recipient_email);
        return "Email sent to " + recipient_email;
    }
}
