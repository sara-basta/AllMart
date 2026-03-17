package com.sara.allmart.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequest(
        @NotBlank(message = "Current password is required")
        String oldPassword,

        @NotBlank(message = "New password is required")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        String newPassword,

        @NotBlank(message = "Confirmation password is required")
        String confirmPassword
) {}