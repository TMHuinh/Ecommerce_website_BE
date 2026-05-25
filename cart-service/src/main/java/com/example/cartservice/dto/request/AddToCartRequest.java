package com.example.cartservice.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddToCartRequest {
    String productID;
    String productName;
    String productImage;
    String size;
    String color;
    int quantity;
    double productPrice;
    int inventoryQuantity;
}
