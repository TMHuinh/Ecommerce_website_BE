package com.example.voucherservice.dto.response;

import java.time.LocalDate;
import java.util.Set;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoucherResponse {
    String id;
    String discountName;
    String description;
    String image;
    double discountAmount;
    boolean isPercentage;
    Double minimumOrderValue;
    boolean freeShip;
    LocalDate expirationDate;
    int quantity;
}
