package com.sara.allmart.dto.response;

import com.sara.allmart.entity.Product;

import java.math.BigDecimal;

public record CartItemResponse (
        Long id,
        Product product,
        Integer quantity,
        BigDecimal price
) {
}
