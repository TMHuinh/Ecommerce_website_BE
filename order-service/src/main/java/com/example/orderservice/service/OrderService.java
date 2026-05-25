package com.example.orderservice.service;

import com.example.dtocommon.kafka.Order_Product.InventoryCheckEvent;
import com.example.dtocommon.kafka.Order_Product.SellProductEvent;
import com.example.orderservice.dto.request.OrderRequest;
import com.example.orderservice.dto.response.ApiResponse;
import com.example.orderservice.dto.response.OrderResponse;
import com.example.orderservice.enums.OrderStatus;
import com.example.orderservice.kafka.producer.OrderProducer;
import com.example.orderservice.mapper.OrderMapper;
import com.example.orderservice.repository.OrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    OrderRepository orderRepository;
    OrderMapper orderMapper;
    OrderProducer orderProducer;

    public ApiResponse<List<OrderResponse>> findByDateOrder(LocalDate date){
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23,59,59);

        var listOrder = orderRepository.findByDateCreatedBetween(startOfDay, endOfDay);

        return ApiResponse.<List<OrderResponse>>builder()
                .result(listOrder.stream()
                        .map(orderMapper::toOrderResponse).toList())
                .build();
    }

    public ApiResponse<OrderResponse> saveOrder(OrderRequest orderRequest) throws JsonProcessingException {
        var order = orderMapper.toOrder(orderRequest);
        order.setStatus(OrderStatus.PROCESSING);
        orderRepository.save(order);

        // Gửi sự kiện "order-created" lên Kafka
        List<InventoryCheckEvent> events = new ArrayList<>();
        order.getOrderDetails().forEach(orderDetail ->
                events.add(
                        InventoryCheckEvent.builder()
                                .productID(orderDetail.getProductID())
                                .color(orderDetail.getColor())
                                .size(orderDetail.getSize())
                                .quantity(orderDetail.getQuantity())
                                .build()
                )
        );

        SellProductEvent event1 = new SellProductEvent(order.getId(),events);
        orderProducer.sendCreateOrderToProduct(event1);

        return ApiResponse.<OrderResponse>builder()
                .result(orderMapper.toOrderResponse(order))
                .build();
    }

}
