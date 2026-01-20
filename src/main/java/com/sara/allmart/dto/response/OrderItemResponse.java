package com.sara.allmart.dto.response;

import java.math.BigDecimal;

public record OrderItemResponse (
        Long id,
        String productName,
        BigDecimal unitPrice,
        int quantity,
        BigDecimal totalPrice
) {
}
