package com.sara.allmart.dto.response;

import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        Integer stockQuantity,
        String categoryName,
        String imageUrl,
        int reviewCount,
        double averageRating
){
}
