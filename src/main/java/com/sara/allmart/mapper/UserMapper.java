package com.sara.allmart.mapper;

import com.sara.allmart.dto.request.UserRequest;
import com.sara.allmart.dto.response.UserResponse;
import com.sara.allmart.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserRequest request) {
        User user = new User();
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setPassword(request.password());
        user.setRole(request.role());
        return user;
    }

    public UserResponse toResponse (User user) {
        Long id = user.getId();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String email = user.getEmail();
        return new UserResponse(id,firstName,lastName,email);
    }
}
