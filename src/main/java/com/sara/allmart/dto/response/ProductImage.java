package com.sara.allmart.dto.response;

public record ProductImage(
        Long id,
        String imageUrl,
        Integer position
) {}