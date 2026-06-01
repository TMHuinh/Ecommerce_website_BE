package com.example.orderservice.enums;

public enum PaymentStatus {
    PENDING("Pending"),
    DIRECT_PAYMENT("Direct Payment"),
    SUCCESSFUL_PAYMENT_WITH_VNPAY("Successful payment with vnpay"),
    VN_PAY("VN Payment"),
    ;

    PaymentStatus(String name) {
        this.name = name;
    }

    private final String name;
}
