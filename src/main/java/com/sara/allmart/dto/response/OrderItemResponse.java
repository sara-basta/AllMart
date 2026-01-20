package com.sara.allmart.dto.response;

import java.math.BigDecimal;

public record OrderItemResponse (
        String productName,
        BigDecimal unitPrice,
        int quantity,
        BigDecimal totalPrice
) {
}
