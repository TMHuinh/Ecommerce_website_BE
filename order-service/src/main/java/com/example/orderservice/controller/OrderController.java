package com.example.orderservice.controller;

import com.example.orderservice.dto.request.OrderRequest;
import com.example.orderservice.dto.request.OrderStatusUpdateRequest;
import com.example.orderservice.dto.response.ApiResponse;
import com.example.orderservice.dto.response.OrderResponse;
import com.example.orderservice.service.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {
    OrderService orderService;

    @PostMapping("/create")
    ApiResponse<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest) throws JsonProcessingException {
        return orderService.saveOrder(orderRequest);
    }

    @GetMapping
    ApiResponse<List<OrderResponse>> getOrder(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return orderService.findByDateOrder(date);
    }

    @GetMapping("/account/{accountID}")
    ApiResponse<List<OrderResponse>> getOrderHistory(@PathVariable String accountID) {
        return orderService.findByAccountID(accountID);
    }

    @GetMapping("/admin/orders")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<List<OrderResponse>> getAllOrdersForAdmin() {
        return orderService.findAllOrders();
    }

    @PatchMapping("/admin/orders/{orderID}/status")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<OrderResponse> updateOrderStatus(
            @PathVariable String orderID,
            @RequestBody OrderStatusUpdateRequest request
    ) {
        return orderService.updateOrderStatus(orderID, request);
    }
}
