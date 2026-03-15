package com.sara.allmart.controller;

import com.sara.allmart.dto.request.AddressRequest;
import com.sara.allmart.dto.request.UpdateProfileRequest;
import com.sara.allmart.dto.request.UserRequest;
import com.sara.allmart.dto.response.AddressResponse;
import com.sara.allmart.dto.response.UserResponse;
import com.sara.allmart.entity.SavedAddress;
import com.sara.allmart.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Validated
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

    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'ADMIN')")
    @PostMapping("/profile/addresses")
    public ResponseEntity<AddressResponse> addAddress(@AuthenticationPrincipal UserDetails user, @Valid @RequestBody AddressRequest request) {
        AddressResponse response = userService.addAddress(user.getUsername(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'ADMIN')")
    @GetMapping("/profile/addresses")
    public ResponseEntity<List<AddressResponse>> getAllAddresses(@AuthenticationPrincipal UserDetails user) {
        List<AddressResponse> response = userService.getAllAddresses(user.getUsername());
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'ADMIN')")
    @DeleteMapping("/profile/addresses/{addressId}")
    public ResponseEntity<Void> deleteAddress(@AuthenticationPrincipal UserDetails user,@PathVariable Long addressId) {
        userService.deleteAddress(user.getUsername(),addressId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'ADMIN')")
    @PatchMapping("/profile/addresses/{addressId}")
    public ResponseEntity<AddressResponse> updateAddress(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable Long addressId,
            @Valid @RequestBody AddressRequest request) {

        AddressResponse response = userService.updateAddress(user.getUsername(), addressId, request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<UserResponse>> getUsers(
            @RequestParam(required = false) Long id,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Page<UserResponse> response = userService.getUsers(id,page,size);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'ADMIN')")
    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getProfile(@AuthenticationPrincipal UserDetails user){
        UserResponse response = userService.getProfile(user.getUsername());
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'ADMIN')")
    @PatchMapping("/profile")
    public ResponseEntity<UserResponse> updateProfile(@AuthenticationPrincipal UserDetails user,@RequestBody UpdateProfileRequest request){
        UserResponse response = userService.updateProfile(user.getUsername(),request);
        return ResponseEntity.ok(response);
    }
}