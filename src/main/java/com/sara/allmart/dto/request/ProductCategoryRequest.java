package com.sara.allmart.dto.request;

import jakarta.validation.constraints.NotNull;

public record ProductCategoryRequest(
        @NotNull
        Long categoryId
) {
}
