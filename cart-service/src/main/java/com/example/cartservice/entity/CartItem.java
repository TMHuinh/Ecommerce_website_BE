package com.example.cartservice.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItem {
    String productID;
    String productName;
    String productImage;
    String size;
    String color;
    int quantity;
    double productPrice;
    int inventoryQuantity;
}
