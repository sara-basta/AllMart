package com.sara.allmart.controller;

import com.sara.allmart.dto.response.ProductResponse;
import com.sara.allmart.service.WishlistService;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishlists")
@Validated
public class WishlistController {
    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getAllWishlist(@AuthenticationPrincipal UserDetails user,
                                                               @RequestParam(defaultValue = "0") @Min(0) int page,
                                                               @RequestParam(defaultValue = "10") int size) {
        Page<ProductResponse> response = wishlistService.getAllWishlist(user.getUsername(), page, size);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @PostMapping("/{productId}")
    public ResponseEntity<ProductResponse> addToWishlist(@AuthenticationPrincipal UserDetails user, @PathVariable Long productId) {
        ProductResponse response = wishlistService.addToWishlist(user.getUsername(), productId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> removeFromWishlist(@AuthenticationPrincipal UserDetails user, @PathVariable Long productId) {
        wishlistService.removeFromWishlist(user.getUsername(),productId);
        return ResponseEntity.noContent().build();
    }

}
