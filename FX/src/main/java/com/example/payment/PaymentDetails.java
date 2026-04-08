package com.example.payment;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentDetails {
    @JsonProperty("card_number")
    private String card_number;
    @JsonProperty("expiry_date")
    private String expiry_date;
    @JsonProperty("cvv")
    private String cvv;

    public PaymentDetails() {}

    //Getters
    @JsonProperty("card_number")
    public String getCard_number() {return card_number;}
    @JsonProperty("expiry_date")
    public String getExpiryDate() {return expiry_date;}
    @JsonProperty("cvv")
    public String getCvv() {return cvv;}


    //Setters
    public void setCard_number(String card_number) {this.card_number = card_number;}
    public void setExpiryDate(String expiry_date) {this.expiry_date = expiry_date;}
    public void setCvv(String cvv) {this.cvv = cvv;}
}
