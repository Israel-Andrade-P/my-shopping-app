package com.zel92.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.math.BigDecimal;

public record ProductRequest(
        @NotEmpty(message = "Field name is required")
        @NotBlank(message = "Field name is required")
        String name,
        String description,
        BigDecimal price,
        Integer quantity,
        @NotEmpty(message = "Field category is required")
        @NotBlank(message = "Field category is required")
        String category
) {
}
