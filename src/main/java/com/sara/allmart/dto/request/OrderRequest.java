package com.sara.allmart.dto.request;

import jakarta.validation.constraints.NotNull;

public record OrderRequest (
        @NotNull
        Long productId
){
}
