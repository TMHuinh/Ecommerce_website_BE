package com.example.dtocommon.kafka.Order_Voucher;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UseVoucherResultEvent {
    String orderID;
    String voucherID;
    boolean result;
}
