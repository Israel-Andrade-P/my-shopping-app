package com.zel92.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record LocationRequest(
                              @NotEmpty(message = "The field country is required")
                              @NotBlank(message = "The field country is required")
                              String country,
                              @NotEmpty(message = "The field city is required")
                              @NotBlank(message = "The field city is required")
                              String city,
                              @NotEmpty(message = "The field street is required")
                              @NotBlank(message = "The field street is required")
                              String street,
                              @NotEmpty(message = "The field zip code is required")
                              @NotBlank(message = "The field zip code is required")
                              String zipCode) {
}
