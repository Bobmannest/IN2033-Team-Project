package com.example.payment;

import com.fasterxml.jackson.annotation.JsonProperty;

//For Payments that will be recorded from IPOS-PU
public class PaymentDetails {
    @JsonProperty("card_type")
    private String card_type;
    @JsonProperty("card_number")
    private String card_number;
    @JsonProperty("expiry_date")
    private String expiry_date;
    @JsonProperty("cvv")
    private String cvv;

    public PaymentDetails() {}

    //Getters
    @JsonProperty("card_type")
    public String getCardType() {return card_type;}
    @JsonProperty("card_number")
    public String getCardNumber() {return card_number;}
    @JsonProperty("expiry_date")
    public String getExpiryDate() {return expiry_date;}
    @JsonProperty("cvv")
    public String getCvv() {return cvv;}


    //Setters
    public void setCardType(String card_type) {this.card_type = card_type;}
    public void setCardNumber(String card_number) {this.card_number = card_number;}
    public void setExpiryDate(String expiry_date) {this.expiry_date = expiry_date;}
    public void setCvv(String cvv) {this.cvv = cvv;}
}
