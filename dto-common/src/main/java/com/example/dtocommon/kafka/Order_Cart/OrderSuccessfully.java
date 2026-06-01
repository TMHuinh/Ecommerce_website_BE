package com.example.dtocommon.kafka.Order_Cart;

import com.example.dtocommon.kafka.Order_Product.InventoryCheckEvent;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderSuccessfully {
    String accountID;
    List<String> listProductID;
}
