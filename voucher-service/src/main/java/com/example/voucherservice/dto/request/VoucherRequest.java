package com.example.voucherservice.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoucherRequest {
    String discountName;
    String description;
    String image;
    double discountAmount;
    boolean isPercentage;
    Double minimumOrderValue;
    boolean freeShip;
    LocalDate expirationDate;
    int quantity;;

}
