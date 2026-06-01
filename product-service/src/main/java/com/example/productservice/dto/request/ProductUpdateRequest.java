package com.example.productservice.dto.request;

import com.example.productservice.entity.Variant;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductUpdateRequest {
    String name;
    double price;
    int discountPercent;
    String description;
    List<Variant> variants;
    String productType;
}
