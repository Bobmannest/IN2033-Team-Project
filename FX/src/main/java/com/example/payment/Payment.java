package com.example.payment;


import com.fasterxml.jackson.annotation.JsonProperty;

public class Payment {
    @JsonProperty("payment_id")
    private int payment_id;
    @JsonProperty("holder_id")
    private int holder_id;
    @JsonProperty("amount")
    private double amount;
    @JsonProperty("payment_date")
    private String payment_date;
    @JsonProperty("notes")
    private String notes;


    public Payment() {}

    //Getters
    @JsonProperty("payment_id")
    public int getPaymentID() {return payment_id;}
    @JsonProperty("holder_id")
    public int getHolderID() {return holder_id;}
    @JsonProperty("amount")
    public double getAmount() {return amount;}
    @JsonProperty("payment_date")
    public String getPaymentDate() {return payment_date;}
    @JsonProperty("notes")
    public String getNotes() {return notes;}

    //Setters
    public void setPaymentID(int payment_id) {this.payment_id = payment_id;}
    public void setHolderID(int holder_id) {this.holder_id = holder_id;}
    public void setAmount(double amount) {this.amount = amount;}
    public void setPaymentDate(String payment_date) {this.payment_date = payment_date;}
    public void setNotes(String notes) {this.notes = notes;}
}
