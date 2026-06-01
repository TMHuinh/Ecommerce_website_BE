package com.example.orderservice.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetail {
    String productID;
    String productName;
    int quantity;
    String color;
    String size;
    double productPrice;
}
