package com.example.payment;

import com.fasterxml.jackson.annotation.JsonProperty;

//For Payments that will be recorded from other subsystems
public class Payment {
    @JsonProperty("first_digits")
    private int first_digits;
    @JsonProperty("last_digits")
    private int last_digits;
    @JsonProperty("expiry_date")
    private String expiry_date;
    @JsonProperty("card_type")
    private String card_type;

    public Payment() {}

    //Getters
    @JsonProperty("first_digits")
    public int getFirstDigits() {return first_digits;}
    @JsonProperty("last_digits")
    public int getLastDigits() {return last_digits;}
    @JsonProperty("expiry_date")
    public String getExpiryDate() {return expiry_date;}
    @JsonProperty("card_type")
    public String getCardType() {return card_type;}

    //Setters
    public void setFirstDigits(int first_digits) {this.first_digits = first_digits;}
    public void setLastDigits(int last_digits) {this.last_digits = last_digits;}
    public void setExpiryDate(String expiry_date) {this.expiry_date = expiry_date;}
    public void setCardType(String card_type) {this.card_type = card_type;}
}
