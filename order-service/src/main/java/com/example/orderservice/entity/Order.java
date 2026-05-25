package com.example.orderservice.entity;


import com.example.orderservice.enums.OrderStatus;
import com.example.orderservice.enums.PaymentStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "Orders")
public class Order {
    @Id
    String id;
    LocalDateTime dateCreated;
    PaymentStatus payment;
    double totalPrice;
    OrderStatus status;
    String consigneeName;
    String consigneeEmail;
    String consigneePhone;
    String address;
    String accountID;
    String voucherID;
    List<OrderDetail> orderDetails;
}
