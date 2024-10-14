package com.zel92.order.dto;

import java.math.BigDecimal;

public record PaymentDTO(String method, BigDecimal amount, String name, String email) {
}
