package com.sara.allmart.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CheckoutItemRequest (
        @NotNull Long productId,
        @Min(1) int quantity
){}