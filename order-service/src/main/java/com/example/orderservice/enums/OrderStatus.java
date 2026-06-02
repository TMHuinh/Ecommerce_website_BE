package com.example.orderservice.enums;

public enum OrderStatus {
    ORDERED("Ordered"),
    CANCELED("Canceled"),
    CANCELLED("Cancelled"),
    PROCESSING("Processing"),
    CONFIRMED("Confirmed"),
    SHIPPING("Shipping"),
    COMPLETED("Completed")
    ;

    OrderStatus(String name) {
        this.name = name;
    }

    private final String name;
}
