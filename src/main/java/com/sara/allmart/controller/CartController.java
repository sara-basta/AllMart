package com.sara.allmart.controller;

import com.sara.allmart.dto.request.CartRequest;
import com.sara.allmart.dto.response.CartResponse;
import com.sara.allmart.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
@Validated
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @PostMapping
    public ResponseEntity<CartResponse> addToCart(@AuthenticationPrincipal UserDetails user,@Valid @RequestBody CartRequest request){
        CartResponse response = cartService.addToCart(user.getUsername(),request.productId(), request.quantity());
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @DeleteMapping("/my-cart/items/{itemId}")
    public ResponseEntity<CartResponse> removeFromCart(@AuthenticationPrincipal UserDetails user, @PathVariable Long itemId) {
        CartResponse response = cartService.removeFromCart(user.getUsername(), itemId);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @PostMapping ("/my-cart/checkout")
    public ResponseEntity<Long> checkout(@AuthenticationPrincipal UserDetails user,@RequestParam Long addressId){
        Long orderId = cartService.checkout(user.getUsername(), addressId);
        return ResponseEntity.ok(orderId);
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @GetMapping("/my-cart")
    public ResponseEntity<CartResponse> getCart(@AuthenticationPrincipal UserDetails user){
        return ResponseEntity.ok(cartService.getCart(user.getUsername()));
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @DeleteMapping ("/my-cart/clear")
    public ResponseEntity<Void> clearCart(@AuthenticationPrincipal UserDetails user){
        cartService.clearCart(user.getUsername());
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @PostMapping("/sync")
    public ResponseEntity<CartResponse> syncCart(@AuthenticationPrincipal UserDetails user,
            @Valid @RequestBody List<CartRequest> items) {
        CartResponse response = cartService.syncCart(user.getUsername(), items);
        return ResponseEntity.ok(response);
    }
    }
