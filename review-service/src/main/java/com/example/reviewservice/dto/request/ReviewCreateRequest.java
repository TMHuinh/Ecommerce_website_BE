package com.example.reviewservice.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewCreateRequest {
    String productID;
    String accountID;
    String content;
    double rating;
    LocalDate createdAt;
}
