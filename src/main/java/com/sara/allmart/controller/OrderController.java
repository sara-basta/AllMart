package com.sara.allmart.controller;

import com.sara.allmart.dto.request.OrderRequest;
import com.sara.allmart.dto.request.OrderStatusRequest;
import com.sara.allmart.dto.response.OrderResponse;
import com.sara.allmart.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<OrderResponse> createOrder (@AuthenticationPrincipal UserDetails user,@Valid @RequestBody OrderRequest request){
        OrderResponse response = orderService.createOrder(user.getUsername(),request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        OrderResponse response = orderService.getOrderById(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/user/{id}")
    public ResponseEntity<List<OrderResponse>> getOrderByUser(@PathVariable Long id){
        List<OrderResponse> response = orderService.getOrderByUser(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponse> updateStatus(@PathVariable Long id, @Valid @RequestBody OrderStatusRequest request){
        OrderResponse response = orderService.updateStatus(id, request.status());
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'ADMIN')")
    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderResponse>> getOrdersHistory(@AuthenticationPrincipal UserDetails user){
        return ResponseEntity.ok(orderService.getOrdersHistory(user.getUsername()));
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @PostMapping("{id}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(@AuthenticationPrincipal UserDetails user,@PathVariable Long id){
        OrderResponse response = orderService.cancelOrder(user.getUsername(),id);
        return ResponseEntity.ok(response);
    }
}
