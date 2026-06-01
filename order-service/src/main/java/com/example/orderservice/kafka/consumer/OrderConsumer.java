package com.example.orderservice.kafka.consumer;

import com.example.dtocommon.kafka.Order_Cart.OrderSuccessfully;
import com.example.dtocommon.kafka.Order_Product.InventoryCheckEvent;
import com.example.dtocommon.kafka.Order_Product.SellProductEvent;
import com.example.dtocommon.kafka.Order_Product.SellProductResultEvent;
import com.example.dtocommon.kafka.Order_Voucher.UseVoucherEvent;
import com.example.dtocommon.kafka.Order_Voucher.UseVoucherResultEvent;
import com.example.orderservice.configuration.OrderWebSocketHandler;
import com.example.orderservice.entity.Order;
import com.example.orderservice.entity.OrderDetail;
import com.example.orderservice.enums.OrderStatus;
import com.example.orderservice.kafka.config.JsonConverter;
import com.example.orderservice.kafka.producer.OrderProducer;
import com.example.orderservice.repository.OrderRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class OrderConsumer {

    OrderRepository orderRepository;
    JsonConverter jsonConverter;
    OrderProducer orderProducer;
    RestTemplate restTemplate;
    SimpMessagingTemplate messagingTemplate;

    private final String createPaymentUrl = "http://localhost:8083/order-service/payment/create_payment";

    @KafkaListener(topics = "use-voucher-response", groupId = "order-group")
    public void handleRollbackOrder(String data) {
        UseVoucherResultEvent event = jsonConverter.fromJson(data, UseVoucherResultEvent.class);

        Order order = orderRepository.findById(event.getOrderID())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (event.isResult()) {
            completeOrder(order);
        } else {
            log.warn("Voucher failed for order {}, rollback product and delete order", order.getId());

            List<InventoryCheckEvent> inventoryCheckEventList = new ArrayList<>();

            order.getOrderDetails().forEach(orderDetail ->
                    inventoryCheckEventList.add(
                            InventoryCheckEvent.builder()
                                    .productID(orderDetail.getProductID())
                                    .color(orderDetail.getColor())
                                    .quantity(orderDetail.getQuantity())
                                    .size(orderDetail.getSize())
                                    .build()
                    )
            );

            SellProductEvent sellProductEvent = SellProductEvent.builder()
                    .orderID(order.getId())
                    .list(inventoryCheckEventList)
                    .build();

            orderProducer.sendRollBackProduct(sellProductEvent);

            orderRepository.delete(order);

            messagingTemplate.convertAndSend("/topic/orders", "Order creation failed");
            OrderWebSocketHandler.broadcast("Order creation failed");
        }
    }

    @KafkaListener(topics = "sell-product-result", groupId = "order-group")
    public void handleSellProductResult(String data) {
        SellProductResultEvent event = jsonConverter.fromJson(data, SellProductResultEvent.class);

        Order order = orderRepository.findById(event.getOrderID())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (event.isResult()) {
            if (order.getVoucherID() == null || order.getVoucherID().isBlank()) {
                completeOrder(order);
                return;
            }

            UseVoucherEvent useVoucherEvent = UseVoucherEvent.builder()
                    .orderID(order.getId())
                    .voucherID(order.getVoucherID())
                    .build();

            orderProducer.sendOrderSuccessToVoucher(useVoucherEvent);
        } else {
            log.warn("Product failed for order {}, delete order. Reason: {}", order.getId(), event.getMessage());

            orderRepository.delete(order);

            messagingTemplate.convertAndSend("/topic/orders", "Order creation failed");
            OrderWebSocketHandler.broadcast("Order creation failed");
        }
    }

    private void completeOrder(Order order) {
        if ("VN_PAY".equals(order.getPayment().name())) {
            try {
                String url = UriComponentsBuilder
                        .fromHttpUrl(createPaymentUrl)
                        .queryParam("amount", (long) order.getTotalPrice())
                        .queryParam("orderID", order.getId())
                        .toUriString();

                HttpHeaders headers = new HttpHeaders();
                headers.setAccept(List.of(MediaType.TEXT_PLAIN));

                HttpEntity<Void> entity = new HttpEntity<>(headers);

                ResponseEntity<String> response = restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        entity,
                        String.class
                );
                log.info("111: ",response.getBody());

                String paymentUrl = response.getBody();

                if (paymentUrl == null || paymentUrl.isBlank()) {
                    log.error("Create VNPay payment failed for order {}", order.getId());

                    messagingTemplate.convertAndSend("/topic/orders", "Fail(Create VNPay payment failed)");
                    OrderWebSocketHandler.broadcast("Fail(Create VNPay payment failed)");

                    return;
                }

                order.setStatus(OrderStatus.ORDERED);
                orderRepository.save(order);

                log.info("VNPay payment link created for order {}", order.getId());

                // Gửi text link VNPay lên frontend
                messagingTemplate.convertAndSend("/topic/orders", paymentUrl);
                OrderWebSocketHandler.broadcast(paymentUrl);

                return;
            } catch (Exception e) {
                log.error("Create VNPay payment error for order {}", order.getId(), e);

                messagingTemplate.convertAndSend("/topic/orders", "Fail(Create VNPay payment error)");
                OrderWebSocketHandler.broadcast("Fail(Create VNPay payment error)");

                return;
            }
        }

        order.setStatus(OrderStatus.ORDERED);
        orderRepository.save(order);

        log.info("Order {} completed successfully", order.getId());

        messagingTemplate.convertAndSend("/topic/orders", "Order successfully processed");
        OrderWebSocketHandler.broadcast("Order successfully processed");

        OrderSuccessfully orderSuccessfully = OrderSuccessfully.builder()
                .accountID(order.getAccountID())
                .listProductID(order.getOrderDetails().stream()
                        .map(OrderDetail::getProductID)
                        .toList())
                .build();

        orderProducer.sendOrderSuccessfullyToCart(orderSuccessfully);
    }
}