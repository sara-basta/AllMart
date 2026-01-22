package com.sara.allmart.controller;

import com.sara.allmart.dto.request.CartRemoveRequest;
import com.sara.allmart.dto.request.CartRequest;
import com.sara.allmart.dto.response.CartResponse;
import com.sara.allmart.service.CartService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public CartResponse addToCart(@RequestBody CartRequest request){
        return cartService.addToCart(request.userId(), request.productId(), request.quantity());
    }

    @DeleteMapping("/remove")
    public CartResponse removeFromCart(@RequestBody CartRemoveRequest request) {
        return cartService.removeFromCart(request.userId(), request.cartItemId());
    }

    @PostMapping ("/checkout")
    public void checkout(@RequestParam Long userId){
        cartService.checkout(userId);
    }

    @GetMapping
    public CartResponse getCart(@RequestParam Long userId){
        return cartService.getCart(userId);
    }

    @PostMapping ("/clear")
    public void clearCart(@RequestParam Long userId){
        cartService.clearCart(userId);
    }
    }
