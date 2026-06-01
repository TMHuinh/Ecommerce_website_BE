package com.example.cartservice.kafka.consumer;


import com.example.cartservice.dto.request.DeleteCartItemRequest;
import com.example.cartservice.kafka.config.JsonConverter;
import com.example.cartservice.repository.CartRepository;
import com.example.cartservice.service.CartService;
import com.example.dtocommon.kafka.Order_Cart.OrderSuccessfully;
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
public class CartConsumer {
    JsonConverter jsonConverter;
    CartService cartService;

    //    lắng nghe order successfully
    @KafkaListener(topics = "order-successfully", groupId = "cart-group")
    public void handlOrderSuccessfully(String data) {
        OrderSuccessfully orderSuccessfully = jsonConverter.fromJson(data, OrderSuccessfully.class);

//        delete cart item
        String accountID = orderSuccessfully.getAccountID();
        orderSuccessfully.getListProductID().stream()
                .forEach(productID -> {
                    cartService.deleteItem(accountID, new DeleteCartItemRequest(productID));
                });
    }
}
