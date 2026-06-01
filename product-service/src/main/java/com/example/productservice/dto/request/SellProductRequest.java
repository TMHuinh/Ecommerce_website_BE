package com.example.productservice.dto.request;

import com.example.productservice.enums.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SellProductRequest {
    String productID;
    Size size;
    String color;
    int quantity;
}
