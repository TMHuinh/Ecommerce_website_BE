package com.example.reviewservice.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Data
@Table(name = "review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String productID;
    String accountID;
    String content;
    double rating;
    LocalDate createdAt;
}
