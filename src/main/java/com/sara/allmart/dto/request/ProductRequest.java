package com.sara.allmart.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

import java.math.BigDecimal;

public record ProductRequest(
        @Schema(description = "Name of the product", example = "Gaming Laptop")
        @NotBlank(message = "Product name is required")
        String name,

        @Schema(description = "Detailed description", example = "High-performance laptop with RTX 4090")
        @NotBlank(message = "Description is required")
        String description,

        @Schema(description = "Price per unit", example = "1299.99")
        @NotNull(message = "Price is required")
        @Positive(message = "Price must be greater than zero")
        BigDecimal price,

        @Schema(description = "Initial stock quantity", example = "50")
        @NotNull(message = "Stock quantity is required")
        @Min(value = 0, message = "Stock cannot be negative")
        Integer stockQuantity,

        @Schema(description = "ID of the category this product belongs to", example = "1")
        @NotNull(message = "Category is required")
        Long categoryId,

        @Schema(description = "List of Cloudinary image URLs", example = "['url1.jpg', 'url2.jpg']")
        List<String> imageUrls
) {
}
