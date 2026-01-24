package com.sara.allmart.controller;

import com.sara.allmart.dto.request.OrderRequest;
import com.sara.allmart.dto.request.OrderStatusRequest;
import com.sara.allmart.dto.response.OrderResponse;
import com.sara.allmart.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder (@Valid @RequestBody OrderRequest request){
        OrderResponse response = orderService.createOrder(request.userId(), request.productId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        OrderResponse response = orderService.getOrderById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<OrderResponse>> getOrderByUser(@PathVariable Long id){
        List<OrderResponse> response = orderService.getOrderByUser(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponse> updateStatus(@PathVariable Long id, @Valid @RequestBody OrderStatusRequest request){
        OrderResponse response = orderService.updateStatus(id, request.status());
        return ResponseEntity.ok(response);
    }

    //TODO: add cancel order
}
