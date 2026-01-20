package com.sara.allmart.mapper;

import com.sara.allmart.dto.response.UserResponse;
import com.sara.allmart.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toResponse (User user) {
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String email = user.getEmail();
        return new UserResponse(firstName,lastName,email);
    }
}
