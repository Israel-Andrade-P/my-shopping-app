package com.zel92.order.dto;

import java.math.BigDecimal;

public record OrderedItemResponse(String productId, BigDecimal price, Integer quantity) {
}
