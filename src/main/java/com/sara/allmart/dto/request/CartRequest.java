package com.sara.allmart.dto.request;

public record CartRequest (Long userId, Long productId, int quantity) {
}
