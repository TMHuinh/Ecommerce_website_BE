package com.example.dtocommon.kafka.Order_Product;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SellProductResultEvent {
    boolean result;
    String orderID;
    String message;
}
