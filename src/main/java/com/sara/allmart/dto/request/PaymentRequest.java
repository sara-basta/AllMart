package com.sara.allmart.dto.request;

import com.sara.allmart.entity.PaymentMethod;
import jakarta.validation.constraints.NotNull;

public record PaymentRequest (
        @NotNull(message = "Order Id is mandatory.")
        Long orderId,

        @NotNull(message = "Payment method is mandatory.")
        PaymentMethod paymentMethod,

        String cardNumber
){
}
