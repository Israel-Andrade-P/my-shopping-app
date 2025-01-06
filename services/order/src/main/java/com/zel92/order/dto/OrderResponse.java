package com.zel92.order.dto;

import java.math.BigDecimal;
import java.util.List;

public record OrderResponse(String orderId, String clientName, BigDecimal totalAmount, List<OrderedItemResponse> items) {
}
