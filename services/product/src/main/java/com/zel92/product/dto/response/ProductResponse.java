package com.zel92.product.dto.response;

import lombok.Builder;

import java.math.BigDecimal;
@Builder
public record ProductResponse(String productId, String name, String description, BigDecimal price) {
}
