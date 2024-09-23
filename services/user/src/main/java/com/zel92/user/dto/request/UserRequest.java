package com.zel92.user.dto.request;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record UserRequest(
        @NotBlank(message = "The field first name is required")
        @NotEmpty(message = "The field first name is required")
        String firstName,
        @NotBlank(message = "The field last name is required")
        @NotEmpty(message = "The field last name is required")
        String lastName,
        @NotBlank(message = "The field email is required")
        @NotEmpty(message = "The field email is required")
        @Email(message = "Please provide a valid email")
        String email,
        @NotBlank(message = "The field password is required")
        @NotEmpty(message = "The field password is required")
        @Size(min = 8, max = 25, message = "Password must contain at least 8 characters")
        String password,
        @NotBlank(message = "The field telephone is required")
        @NotEmpty(message = "The field telephone is required")
        String telephone,
        @NotNull(message = "The field Date of Birth is required")
        LocalDate dob,
        LocationRequest location
) {
}
