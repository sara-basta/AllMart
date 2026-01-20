package com.sara.allmart.mapper;

import com.sara.allmart.dto.response.OrderItemResponse;
import com.sara.allmart.dto.response.OrderResponse;
import com.sara.allmart.entity.Order;
import com.sara.allmart.entity.OrderItem;
import com.sara.allmart.entity.OrderStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class OrderMapper {

    public OrderResponse toResponse(Order order) {
        Long id = order.getId();
        OrderStatus status = order.getStatus();
        BigDecimal totalAmount = order.getTotalAmount();
        String shippingCity = order.getShippingAddress().getCity();
        LocalDateTime orderDate = order.getCreatedAt();
        List<OrderItemResponse> items = order.getItems().stream()
                .map(this::toItemResponse)
                .toList();
        return new OrderResponse(id,status,totalAmount,shippingCity,orderDate,items);
    }


    public OrderItemResponse toItemResponse(OrderItem orderItem) {
        String productName = orderItem.getProduct().getName();
        BigDecimal unitPrice = orderItem.getPriceAtPurchase();
        int quantity= orderItem.getQuantity();
        BigDecimal totalPrice= unitPrice.multiply(BigDecimal.valueOf(quantity));

        return new OrderItemResponse (productName,unitPrice,quantity,totalPrice);
    }
}
