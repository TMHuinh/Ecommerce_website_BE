package com.example.orderservice.kafka.producer;

import com.example.dtocommon.kafka.Order_Cart.OrderSuccessfully;
import com.example.dtocommon.kafka.Order_Voucher.UseVoucherEvent;
import com.example.dtocommon.kafka.Order_Voucher.UseVoucherResultEvent;
import com.example.dtocommon.kafka.Order_Product.SellProductEvent;
import com.example.orderservice.kafka.config.JsonConverter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class OrderProducer {
    KafkaTemplate<String, String > kafkaTemplate;
    JsonConverter jsonConverter;
    final static String topic_create_order = "use-voucher";
    final static String topic_sell_product = "sell-product";
    final static String topic_rollback_product = "rollback-product";
    final static String topic_order_successfully= "order-successfully";

    public void sendOrderSuccessToVoucher(UseVoucherEvent orderVoucherEvent){
        kafkaTemplate.send(topic_create_order, jsonConverter.toJson(orderVoucherEvent));
    }

    public void sendCreateOrderToProduct(SellProductEvent event){
        kafkaTemplate.send(topic_sell_product, jsonConverter.toJson(event));
    }

    public void sendRollBackProduct(SellProductEvent event){
        kafkaTemplate.send(topic_rollback_product, jsonConverter.toJson(event));
    }

    public void sendOrderSuccessfullyToCart(OrderSuccessfully event){
        kafkaTemplate.send(topic_order_successfully, jsonConverter.toJson(event));
    }
}
