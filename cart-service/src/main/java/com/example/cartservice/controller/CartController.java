package com.example.cartservice.controller;

import com.example.cartservice.dto.ApiResponse;
import com.example.cartservice.dto.request.CartCreationRequest;
import com.example.cartservice.dto.request.AddToCartRequest;
import com.example.cartservice.dto.request.DeleteCartItemRequest;
import com.example.cartservice.dto.request.EditCartItemQuantityRequest;
import com.example.cartservice.dto.response.CartResponse;
import com.example.cartservice.mapper.CartMapper;
import com.example.cartservice.service.CartService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartController {
    CartService cartService;
    CartMapper cartMapper;
    @PostMapping("/add-item/{accountID}")
    ApiResponse<CartResponse> addItemToCart(@PathVariable String accountID,
                                            @RequestBody AddToCartRequest request){
        return cartService.addItemToCart(accountID, request);
    }

    @GetMapping
    ApiResponse<List<CartResponse>> getAll(){
        return cartService.getAll();
    }

    @GetMapping("/get/{accountID}")
    ApiResponse<CartResponse> getByAccountID(@PathVariable String accountID){
        return cartService.getByAccountID(accountID);
    }
    @PostMapping("/create")
    ApiResponse<CartResponse> createCart(@RequestBody CartCreationRequest request){
        return cartService.createCart(request);
    }

    @PutMapping("/{accountID}")
    ApiResponse<CartResponse> editCartItemQuantity(@PathVariable String accountID,
                                                   @RequestBody EditCartItemQuantityRequest request){
        return cartService.editCartItemQuantity(accountID, request);
    }

    @DeleteMapping("/delete-item/{accountID}")
    ApiResponse<CartResponse> deleteItem(@PathVariable String accountID,
                                         @RequestParam String productID){
        DeleteCartItemRequest request = new DeleteCartItemRequest();
        request.setProductID(productID);
        return cartService.deleteItem(accountID, request);
    }
    @KafkaListener(topics = "account-create")
    public void listenAccountCreate(String accountID){
        CartCreationRequest request = new CartCreationRequest();
        request.setAccountID(accountID);
        cartService.createCart(request);
    }
}
