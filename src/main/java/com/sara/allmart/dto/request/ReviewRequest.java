package com.sara.allmart.dto.request;

public record ReviewRequest(
        int rating,
        String comment
) {
}
