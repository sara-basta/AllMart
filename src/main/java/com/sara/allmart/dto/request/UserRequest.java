package com.sara.allmart.dto.request;

import com.sara.allmart.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserRequest(
        @NotBlank(message = "First name is required")
        String firstName,
        @NotBlank(message = "Last name is required")
        String lastName,
        @NotBlank(message = "Email is required")
        @Email(regexp = "^.+@.+$",message = "Email format is incorrect")
        String email,
        @NotBlank(message = "Password is required")
        String password,
        @NotNull(message = "Role is required")
        Role role
) {
}
