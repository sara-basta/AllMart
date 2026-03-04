package com.sara.allmart.mapper;

import com.sara.allmart.dto.response.CartItemResponse;
import com.sara.allmart.dto.response.CartResponse;
import com.sara.allmart.dto.response.ProductResponse;
import com.sara.allmart.entity.Cart;
import com.sara.allmart.entity.CartItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class CartMapper {

    private final ProductMapper productMapper;

    public CartMapper(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public CartResponse toResponse(Cart cart){
        Long id = cart.getId();
        List<CartItemResponse> items = cart.getItems().stream()
                .map(this::toItemResponse)
                .toList();
        BigDecimal totalPrice = cart.getTotalPrice();
        return new CartResponse(id,items,totalPrice);
    }

    public CartItemResponse toItemResponse(CartItem cartItem){
        Long id = cartItem.getId();
        ProductResponse product = productMapper.toResponse(cartItem.getProduct());
        Integer quantity = cartItem.getQuantity();
        BigDecimal unitPrice = cartItem.getPrice();
        BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));
        return new CartItemResponse(id,product,quantity,totalPrice);
    }
}
