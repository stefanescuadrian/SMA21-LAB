package com.upt.cti.smartwallet.model;

import java.io.Serializable;

public class Payment implements Serializable {
    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public double getPaymentCost() {
        return paymentCost;
    }

    public void setPaymentCost(double paymentCost) {
        this.paymentCost = paymentCost;
    }

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String timestamp;
    private double paymentCost;
    private String paymentName;
    private String paymentType;

    public Payment() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Payment(String timestamp, double cost, String name, String type) {
        this.timestamp = timestamp;
        this.paymentCost = cost;
        this.paymentName = name;
        this.paymentType = type;
    }

    public Payment recreatePayment(){
        Payment payment = new Payment();
        payment.paymentCost = paymentCost;
        payment.paymentName = new String(paymentName);
        payment.paymentType = new String(paymentType);
        payment.timestamp = new String(timestamp);
        return payment;
    }
}
