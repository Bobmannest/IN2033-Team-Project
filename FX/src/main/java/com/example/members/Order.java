package com.example.members;

import java.time.LocalDateTime;

public class Order {
    private String orderId;
    private String accountNo;
    private LocalDateTime orderDate;
    private double totalAmount;
    private String deliveryAddress;

    public Order(String orderId, String accountNo, LocalDateTime orderDate,
                 double totalAmount, String deliveryAddress) {
        this.orderId = orderId;
        this.accountNo = accountNo;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.deliveryAddress = deliveryAddress;
    }

    public String getOrderId() { return orderId; }
    public String getAccountNo() { return accountNo; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public double getTotalAmount() { return totalAmount; }
    public String getDeliveryAddress() { return deliveryAddress; }
}