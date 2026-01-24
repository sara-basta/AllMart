package com.sara.allmart.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequest (
        @NotBlank(message = "Category name is required")
        String name,
        @NotBlank(message = "Description is required")
        String description
){
}
