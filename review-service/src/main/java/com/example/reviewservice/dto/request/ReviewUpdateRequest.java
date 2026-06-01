package com.example.reviewservice.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewUpdateRequest {
    String content;
    LocalDateTime createdAt;
}
