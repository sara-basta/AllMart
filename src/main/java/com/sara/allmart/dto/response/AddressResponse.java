package com.sara.allmart.dto.response;

public record AddressResponse (
        Long id,
        String Street,
        String city,
        String zipCode
){
}
