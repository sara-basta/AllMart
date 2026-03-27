package com.sara.allmart.dto.request;

import com.sara.allmart.entity.Address;
import com.sara.allmart.entity.PaymentMethod;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record GuestCheckoutRequest(
        @NotBlank @Email String email,
        @NotNull Address shippingAddress,
        @NotNull PaymentMethod paymentMethod,
        @NotEmpty List<CheckoutItemRequest> items
) {
}
