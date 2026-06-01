package com.example.dtocommon.kafka.Order_Product;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InventoryCheckEvent {
    String productID;
    int quantity;
    String color;
    String size;
}
