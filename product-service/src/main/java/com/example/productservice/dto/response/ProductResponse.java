package com.example.productservice.dto.response;

import com.example.productservice.entity.ProductType;
import com.example.productservice.entity.Variant;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {
    String id;
    String name;
    double price;
    int discountPercent;
    String description;
    double avgStar;
    List<Variant> variants;
    String productType;
}
