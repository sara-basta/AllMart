package com.sara.allmart.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProductPriceRequest(
        @NotNull(message = "Price is required")
        @Positive(message = "Price must be greater than zero")
        BigDecimal price
) {
}
