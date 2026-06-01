package com.example.productservice.entity;

import com.example.productservice.enums.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Variant {
    Size size;
    String color;
    String imageURL;
    int inventoryQuantity;
}
