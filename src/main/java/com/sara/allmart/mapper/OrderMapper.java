package com.sara.allmart.mapper;

import com.sara.allmart.dto.response.OrderItemResponse;
import com.sara.allmart.dto.response.OrderResponse;
import com.sara.allmart.entity.Address;
import com.sara.allmart.entity.Order;
import com.sara.allmart.entity.OrderItem;
import com.sara.allmart.entity.OrderStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class OrderMapper {

    public static OrderResponse toResponse(Order order) {
        Long id = order.getId();
        OrderStatus status = order.getStatus();
        BigDecimal totalAmount = order.getTotalAmount();
        Address shippingAddress = order.getShippingAddress();
        LocalDateTime orderDate = order.getCreatedAt();
        List<OrderItemResponse> items = order.getItems().stream()
                .map(OrderMapper::toItemResponse)
                .toList();
        return new OrderResponse(id,status,totalAmount,shippingAddress,orderDate,items);
    }


    public static OrderItemResponse toItemResponse(OrderItem orderItem) {
        Long productId = orderItem.getProduct().getId();
        String productName = orderItem.getProduct().getName();
        BigDecimal unitPrice = orderItem.getProduct().getPrice();
        int quantity= orderItem.getQuantity();
        BigDecimal totalPrice= unitPrice.multiply(BigDecimal.valueOf(quantity));

        return new OrderItemResponse (productId,productName,unitPrice,quantity,totalPrice);
    }
}
