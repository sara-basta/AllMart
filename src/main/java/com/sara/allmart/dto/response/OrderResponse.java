package com.sara.allmart.dto.response;

import com.sara.allmart.entity.Address;
import com.sara.allmart.entity.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        OrderStatus status,
        BigDecimal totalAmount,
        Address shippingAddress,
        LocalDateTime orderDate,
        List<OrderItemResponse> items
) {
}
