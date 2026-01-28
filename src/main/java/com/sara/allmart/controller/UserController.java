package com.sara.allmart.controller;

import com.sara.allmart.dto.request.AddressRequest;
import com.sara.allmart.dto.request.UserRequest;
import com.sara.allmart.dto.response.UserResponse;
import com.sara.allmart.entity.SavedAddress;
import com.sara.allmart.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest request) {
        UserResponse response = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        UserResponse response = userService.getUser(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @PostMapping("/profile/addresses")
    public ResponseEntity<SavedAddress> addAddress(@AuthenticationPrincipal UserDetails user, @Valid @RequestBody AddressRequest request) {
        SavedAddress response = userService.addAddress(user.getUsername(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @GetMapping("/profile/addresses")
    public ResponseEntity<List<SavedAddress>> getAllAddresses(@AuthenticationPrincipal UserDetails user) {
        List<SavedAddress> response = userService.getAllAddresses(user.getUsername());
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @DeleteMapping("/profile/addresses/{addressId}")
    public ResponseEntity<Void> deleteAddress(@AuthenticationPrincipal UserDetails user,@PathVariable Long addressId) {
        userService.deleteAddress(user.getUsername(),addressId);
        return ResponseEntity.noContent().build();
    }

    //TODO: update user and get all users(pagination) for admins & add get profile for customers
}