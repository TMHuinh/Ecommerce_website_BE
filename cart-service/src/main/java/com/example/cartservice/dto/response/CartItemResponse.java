package com.example.cartservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItemResponse {
    String productID;
    String productName;
    String productImage;
    String size;
    String color;
    int quantity;
    double productPrice;
    int inventoryQuantity;
}
