package com.sara.allmart.dto.response;

public record ReviewResponse (
        Long id,
        int rating,
        String comment,
        String firstName
){
}
