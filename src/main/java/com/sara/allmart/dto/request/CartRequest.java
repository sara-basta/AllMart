package com.sara.allmart.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CartRequest (
        @NotNull
        Long productId,
        @Positive @Min(1)
        int quantity
) {
}
