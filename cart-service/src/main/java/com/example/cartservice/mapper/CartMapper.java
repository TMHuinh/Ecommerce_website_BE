package com.example.cartservice.mapper;

import com.example.cartservice.dto.request.CartCreationRequest;
import com.example.cartservice.dto.response.CartResponse;
import com.example.cartservice.entity.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartResponse toCartResponse(Cart cart);

    Cart toCart(CartCreationRequest request);
}
