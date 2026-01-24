package com.sara.allmart.controller;

import com.sara.allmart.dto.request.CartRequest;
import com.sara.allmart.dto.response.CartResponse;
import com.sara.allmart.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping
    public ResponseEntity<CartResponse> addToCart(@Valid @RequestBody CartRequest request){
        CartResponse response = cartService.addToCart(request.userId(), request.productId(), request.quantity());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{userId}/items/{itemId}")
    public ResponseEntity<CartResponse> removeFromCart(@PathVariable Long userId, @PathVariable Long itemId) {
        CartResponse response = cartService.removeFromCart(userId, itemId);
        return ResponseEntity.ok(response);
    }

    @PostMapping ("/{userId}/checkout")
    public ResponseEntity<String> checkout(@PathVariable Long userId){
        cartService.checkout(userId);
        return ResponseEntity.ok("Checkout successful");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CartResponse> getCart(@PathVariable Long userId){
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    @PostMapping ("/{userId}")
    public ResponseEntity<Void> clearCart(@PathVariable Long userId){
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
    }
