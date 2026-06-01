package com.example.orderservice.dto.request;

import com.example.orderservice.enums.OrderStatus;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderStatusUpdateRequest {
    OrderStatus status;
}
