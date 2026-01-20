package com.sara.allmart.controller;

import com.sara.allmart.dto.request.OrderRequest;
import com.sara.allmart.dto.request.OrderStatusRequest;
import com.sara.allmart.dto.response.OrderResponse;
import com.sara.allmart.entity.OrderStatus;
import com.sara.allmart.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create")
    public OrderResponse createOrder (@RequestBody OrderRequest request){
        return orderService.createOrder(request.userId(),request.productId());
    }

    @GetMapping("/{id}")
    public OrderResponse getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    @GetMapping("/user/{id}")
    public List<OrderResponse> getOrderByUser(@PathVariable Long id){
        return orderService.getOrderByUser(id);
    }

    @PutMapping("/{id}/status")
    public OrderResponse updateStatus(@PathVariable Long id, @RequestBody OrderStatusRequest request){
        return orderService.updateStatus(id,request.status());
    }
}
