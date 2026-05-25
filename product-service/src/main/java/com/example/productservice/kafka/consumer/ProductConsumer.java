package com.example.productservice.kafka.consumer;

import com.example.dtocommon.kafka.Order_Product.InventoryCheckEvent;
import com.example.dtocommon.kafka.Order_Product.SellProductEvent;
import com.example.dtocommon.kafka.Order_Product.SellProductResultEvent;
import com.example.productservice.dto.request.SellProductRequest;
import com.example.productservice.dto.response.ApiResponse;
import com.example.productservice.kafka.config.JsonConverter;
import com.example.productservice.kafka.producer.ProductProducer;
import com.example.productservice.mapper.ProductMapper;
import com.example.productservice.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProductConsumer {
    JsonConverter jsonConverter;
    ProductService productService;
    ProductMapper productMapper;
    ProductProducer productProducer;

    //    lắng nghe sell product từ order
    @KafkaListener(topics = "sell-product", groupId = "product-group")
    public void handleRollbackOrder(String data) {
        SellProductEvent events = jsonConverter.fromJson(data, SellProductEvent.class);

        SellProductResultEvent event = SellProductResultEvent.builder()
                .orderID(events.getOrderID())
                .result(true)
                .build();

        List<InventoryCheckEvent> inventoryCheckEventList = events.getList();
        List<SellProductRequest> processRequests = new ArrayList<>();
        for (InventoryCheckEvent inventoryCheckEvent : inventoryCheckEventList) {
            ApiResponse<?> response = productService.sellProduct(productMapper.toSellProductRequest(inventoryCheckEvent));
            if(response.getCode() != 1000){
                event.setResult(false);
                processRequests.stream().forEach(
                        sellProductRequest -> productService.rollbackSellProduct(sellProductRequest)
                );
                event.setMessage("Sell product fail");
                productProducer.sellProductResultEvent(event);
                return;
            }else {
                processRequests.add(productMapper.toSellProductRequest(inventoryCheckEvent));
            }
        }

        event.setMessage("Sell product success");
        productProducer.sellProductResultEvent(event);
    }

    @KafkaListener(topics = "rollback-product", groupId = "product-group")
    public void handleRollbackProduct(String data) {
        SellProductEvent events = jsonConverter.fromJson(data, SellProductEvent.class);
        List<InventoryCheckEvent> inventoryCheckEventList = events.getList();

        for (InventoryCheckEvent inventoryCheckEvent : inventoryCheckEventList) {
            productService.rollbackSellProduct(productMapper.toSellProductRequest(inventoryCheckEvent));
        }
    }
}
