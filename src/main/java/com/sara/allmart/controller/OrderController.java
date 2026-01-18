package com.sara.allmart.controller;

import com.sara.allmart.dto.request.OrderRequest;
import com.sara.allmart.dto.response.OrderResponse;
import com.sara.allmart.entity.Order;
import com.sara.allmart.service.OrderService;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/user/order")
    public OrderResponse createOrder (@RequestBody OrderRequest request){
        return orderService.createOrder(request.userId(),request.productId());
    }

    @GetMapping("/user/order/{id}")
    public OrderResponse getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }
}
