package com.example.orderservice.vnpay.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentInfo {
    String vnp_Amount;
    String vnp_BankCode;
    String vnp_TxnRef;
    String vnp_CardType;
    String vnp_ResponseCode;
}
