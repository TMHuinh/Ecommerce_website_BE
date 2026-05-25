package com.example.cartservice.service;

import com.example.cartservice.dto.ApiResponse;
import com.example.cartservice.dto.request.CartCreationRequest;
import com.example.cartservice.dto.request.AddToCartRequest;
import com.example.cartservice.dto.request.DeleteCartItemRequest;
import com.example.cartservice.dto.request.EditCartItemQuantityRequest;
import com.example.cartservice.dto.response.CartResponse;
import com.example.cartservice.entity.Cart;
import com.example.cartservice.entity.CartItem;
import com.example.cartservice.exception.AppException;
import com.example.cartservice.enums.ErrorCode;
import com.example.cartservice.mapper.CartItemMapper;
import com.example.cartservice.mapper.CartMapper;
import com.example.cartservice.repository.CartRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CartService {
    CartRepository cartRepository;
    CartMapper cartMapper;
    CartItemMapper cartItemMapper;


    public ApiResponse<CartResponse> addItemToCart(String accountID, AddToCartRequest request){
        Cart cart = cartRepository.findByAccountID(accountID)
                .orElseThrow(()->new AppException(ErrorCode.CART_NOT_FOUND));

//        kiểm tra xem cartItemRequest đã có trong cart chưa
        Optional<CartItem> existingCartItem = cart.getItems().stream()
                .filter(item -> item.getProductID().equals(request.getProductID()))
                .findFirst();

        if(existingCartItem.isPresent()){
            CartItem item = existingCartItem.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
        }else{
            CartItem cartItem = cartItemMapper.toCartItem(request);
            cart.getItems().add(cartItem);
        }

        return ApiResponse.<CartResponse>builder()
                .result(cartMapper.toCartResponse(cartRepository.save(cart)))
                .build();
    }

    public ApiResponse<CartResponse> editCartItemQuantity(String accountID, EditCartItemQuantityRequest request){
        Cart cart = cartRepository.findByAccountID(accountID)
                .orElseThrow(()->new AppException(ErrorCode.CART_NOT_FOUND));

//        kiểm tra xem cartItemRequest đã có trong cart chưa
        Optional<CartItem> existingCartItem = cart.getItems().stream()
                .filter(item -> item.getProductID().equals(request.getProductID()))
                .findFirst();

        existingCartItem.get().setQuantity(request.getQuantity());

        return ApiResponse.<CartResponse>builder()
                .result(cartMapper.toCartResponse(cartRepository.save(cart)))
                .build();
    }

    public ApiResponse<CartResponse> deleteItem(String accountID, DeleteCartItemRequest request){
        Cart cart = cartRepository.findByAccountID(accountID)
                .orElseThrow(()->new AppException(ErrorCode.CART_NOT_FOUND));

        cart.getItems().removeIf(item -> item.getProductID().equals(request.getProductID()));

        return ApiResponse.<CartResponse>builder()
                .result(cartMapper.toCartResponse(cartRepository.save(cart)))
                .build();
    }

    public ApiResponse<CartResponse> createCart(CartCreationRequest request){
        Cart cart = cartMapper.toCart(request);
        cart.setItems(Collections.emptyList());
        return ApiResponse.<CartResponse>builder()
                .result(cartMapper.toCartResponse(cartRepository.save(cart)))
                .build();
    }

    public ApiResponse<List<CartResponse>> getAll(){
        return ApiResponse.<List<CartResponse>>builder()
                .result(cartRepository.findAll().stream()
                        .map(cartMapper::toCartResponse).toList())
                .build();
    }

    public ApiResponse<CartResponse> getByAccountID(String accountID){
        return ApiResponse.<CartResponse>builder()
                .result(cartMapper.toCartResponse(cartRepository.findByAccountID(accountID)
                        .orElseThrow(()->new AppException(ErrorCode.CART_NOT_FOUND))))
                .build();
    }
}
