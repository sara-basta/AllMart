package com.sara.allmart.dto.response;

public record UserResponse(
        Long id,
        String firstName,
        String lastName,
        String email
) {
}
