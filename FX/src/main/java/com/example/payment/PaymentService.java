package com.example.payment;

import org.springframework.stereotype.Service;

import java.time.YearMonth;

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

    //Processes payment details sent by other subsystems as well as checkout page input
    public boolean processPayment(PaymentDetails paymentDetails) {
        String cardType = paymentDetails.getCardType();
        String cardNum = paymentDetails.getCardNumber();
        String expiry = paymentDetails.getExpiryDate();
        String cvv = paymentDetails.getCvv();

        String cvvPattern;
        if (cardType.equals("AmEx")) {
            cvvPattern = "\\d{4}";
        } else {
            cvvPattern = "\\d{3}";
        }

        System.out.println("Processing" + cardType + "Payment...");
        if (!isCardValid(cardNum, cardType) || !isExpiryValid(expiry) || !cvv.matches(cvvPattern)) {
            System.out.println("Invalid payment details.");
            return false;
        }

        System.out.println("Payment is valid");
        return true;
    }

    //Checks expiry date format
    private boolean isExpiryValid(String expiry) {
        if (!expiry.matches("\\d{2}/\\d{2}")) {return false;};

        String[] parts = expiry.split("/");
        int month = Integer.parseInt(parts[0]);
        int year = Integer.parseInt(parts[1]) + 2000;

        if (month < 1 || month > 12) return false;

        YearMonth cardExpiry = YearMonth.of(year, month);
        YearMonth now = YearMonth.now();

        return !cardExpiry.isBefore(now);
    }

    //Checks card format for Visa, MasterCard and AmEx
    private boolean isCardValid(String cardNum, String cardType) {
        if (cardNum == null) return false;

        switch (cardType) {
            case ("Visa"):
                if (!cardNum.matches("4\\d{15}")) {return false;}
                break;
            case ("Mastercard"):
                if (!cardNum.matches("5[1-5]\\d{14}")) {return false;}
                break;
            case ("AmEx"):
                if (!cardNum.matches("3[47]\\d{13}")) {return false;}
                break;
            default:
                return false;
        }
        return Luhns(cardNum);
    }

    //Luhn's algorithm for validating card numbers
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
