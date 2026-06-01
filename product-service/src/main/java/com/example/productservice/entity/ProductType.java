package com.example.productservice.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "ProductType")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class ProductType {
    @Id
    String id;
    String name;
    String description;
}
