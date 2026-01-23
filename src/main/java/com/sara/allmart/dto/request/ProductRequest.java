package com.sara.allmart.dto.request;

import java.math.BigDecimal;

public record ProductRequest(
        String name,
        String description,
        BigDecimal price,
        Integer stockQuantity,
        Long categoryId
) {
}
