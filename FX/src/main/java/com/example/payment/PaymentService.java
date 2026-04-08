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

    public void recordPayment(Payment payment) {
        //Records payment to database
    }

    public boolean processPayment(PaymentDetails paymentDetails) {
        String cardNum = paymentDetails.getCard_number();
        String expiry = paymentDetails.getExpiryDate();
        String cvv = paymentDetails.getCvv();

        if (!isCardValid(cardNum) || !expiry.matches("\\d{2}/\\d{2}") || !cvv.matches("\\d{3}")) {
            System.out.println("Invalid payment details.");
            return false;
        }

        System.out.println("Payment is valid, processing...");
        return true;
    }

    // Checks format and Luhn's algorithm
    private boolean isCardValid(String cardNum) {
        if (cardNum == null || !cardNum.matches("\\d{15,16}")) {
            return false;
        }
        return Luhns(cardNum);
    }

    private boolean Luhns(String cardNum) {
        char[] cardDigits = cardNum.toCharArray();
        int sum = 0;
        boolean shouldDoubleDigit = false;

        for (int i = cardDigits.length - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(cardDigits[i]);
            if (shouldDoubleDigit) {
                digit *= 2;
                if (digit > 9) { digit -= 9; }
            }
            sum += digit;
            shouldDoubleDigit = !shouldDoubleDigit;
        }

        return sum % 10 == 0;
    }
}
