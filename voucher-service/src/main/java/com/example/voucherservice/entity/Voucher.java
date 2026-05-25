package com.example.voucherservice.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "vouchers")
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String discountName;
    String description;
    String image;
    double discountAmount; // mức giảm(tiền hoặc % giảm)
    boolean isPercentage; // mức giảm là % hoặc tiền
    Double minimumOrderValue; //giá trị tối thiểu của đơn hàng để áp dụng giảm
    boolean freeShip;
    LocalDate expirationDate;
    int quantity;
}
