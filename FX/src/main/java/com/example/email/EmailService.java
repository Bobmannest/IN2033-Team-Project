package com.example.email;

import com.example.basket.BasketList;
import com.example.catalogue.CatalogueItem;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import java.util.List;
import java.util.Properties;

@Service
public class EmailService {
    @Value("${spring.mail.username}")
    private String username;
    @Value("${spring.mail.password}")
    private String password;

    public void sendPurchaseEmail(
            String recipientName,
            String recipientEmail,
            String recipientAddress,
            String track_id)
    {

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "465");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.socketFactory.port", "465");
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        Session session = Session.getInstance(prop,
                new jakarta.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("ipospu33@gmail.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(recipientEmail)
            );
            message.setSubject("IPOS Purchase Receipt");
            message.setText("Purchase Successful!\n" +
                    "\nReceipt: " +
                    "\nName: " + recipientName +
                    "\nEmail: " + recipientEmail +
                    "\nAddress: " + recipientAddress +
                    "\n\nPurchased Items:\n" +
                    getPurchasedItems() +
                    "\n\nTracking Link: http://localhost:8080/api/orders/track?id=" + track_id
            );

            Transport.send(message);
            System.out.println("Sent Email Successfully");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private String getPurchasedItems() {
        StringBuilder itemString = new StringBuilder();
        for (CatalogueItem item : BasketList.getBasketItems()) {
            String roundedPrice = String.format("%.2f", item.getPackage_cost());
            itemString.append("\n- ").append(item.getDescription())
                    .append(" | ").append(item.getPackage_type())
                    .append(" | £").append(roundedPrice);
        }
        return itemString.toString();
    }

    public void sendTestEmail(String recipient_email) {
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "465");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.socketFactory.port", "465");
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        Session session = Session.getInstance(prop,
                new jakarta.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("ipospu33@gmail.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(recipient_email)
            );
            message.setSubject("Testing IPOS-PU Email Service");
            message.setText("[This is an automatically generated email],"
                    + "\n\n [Test Successful]");

            Transport.send(message);
            System.out.println("Done");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendRegistrationEmail(String recipientEmail, String generatedPassword) {
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "465");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.socketFactory.port", "465");
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        Session session = Session.getInstance(prop,
                new jakarta.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("ipospu33@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Your CuraTech Account Details");
            message.setText(
                    "Welcome to CuraTech!\n\n" +
                            "Your account has been created. Here are your login details:\n\n" +
                            "Email: " + recipientEmail + "\n" +
                            "Password: " + generatedPassword + "\n\n" +
                            "You will be asked to change your password on first login.\n\n" +
                            "[This is an automatically generated email]"
            );
            Transport.send(message);
            System.out.println("Registration email sent to " + recipientEmail);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendPasswordResetEmail(String recipientEmail, String tempPass) {
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "465");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.socketFactory.port", "465");
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        Session session = Session.getInstance(prop,
                new jakarta.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("ipospu33@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("CuraTech - Password Reset Request");
            message.setText(
                    "We received a request to reset the password for your CuraTech account.\n\n" +
                            "Your temporary password is : " + tempPass + "\n\n" +
                            "For security reasons, you will be asked to change this password when you next log in.\n\n" +
                            "If you did not request a password reset, please contact your system administrator immediately.\n\n" +
                            "You will be asked to change your password on first login.\n\n" +
                            "CuraTech Pharmacy Portal\n" +
                            "[This is an automatically generated email - please do not reply]\n"
            );
            Transport.send(message);
            System.out.println("Password reset email sent to " + recipientEmail);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
