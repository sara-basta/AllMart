package com.sara.allmart.dto.request;

import com.sara.allmart.entity.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record OrderStatusRequest(
        @NotNull(message = "Status is required")
        OrderStatus status
) {
}
