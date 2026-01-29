package com.sara.allmart.mapper;

import com.sara.allmart.dto.response.AddressResponse;
import com.sara.allmart.entity.SavedAddress;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public AddressResponse toResponse(SavedAddress address){
        Long id = address.getId();
        String street = address.getStreet();
        String city = address.getCity();
        String zipCode = address.getZipCode();
        return new AddressResponse(id,street,city,zipCode);
    }
}
