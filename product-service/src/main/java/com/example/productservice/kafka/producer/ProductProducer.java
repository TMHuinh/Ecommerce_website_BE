package com.example.productservice.kafka.producer;

import com.example.dtocommon.kafka.Order_Product.SellProductResultEvent;
import com.example.productservice.kafka.config.JsonConverter;
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
public class ProductProducer {
    KafkaTemplate<String, String > kafkaTemplate;
    JsonConverter jsonConverter;
    final static String topic_sell_product_result = "sell-product-result";

    public void sellProductResultEvent(SellProductResultEvent event) {
        kafkaTemplate.send(topic_sell_product_result, jsonConverter.toJson(event));
    }
}
