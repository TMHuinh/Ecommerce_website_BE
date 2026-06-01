package com.example.productservice.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "Product")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class Product {
    @Id
    String id;
    String name;
    double price;
    int discountPercent;
    String description;
    double avgStar;
    List<Variant> variants;
    String productType;
}
