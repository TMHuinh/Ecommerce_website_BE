package com.example.dtocommon.kafka.Order_Product;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SellProductEvent {
    String orderID;
    List<InventoryCheckEvent> list;
}
