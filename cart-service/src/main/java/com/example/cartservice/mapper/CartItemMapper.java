package com.example.cartservice.mapper;

import com.example.cartservice.dto.request.AddToCartRequest;
import com.example.cartservice.dto.request.DeleteCartItemRequest;
import com.example.cartservice.entity.CartItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartItemMapper {

    CartItem toCartItem(AddToCartRequest request);

}
