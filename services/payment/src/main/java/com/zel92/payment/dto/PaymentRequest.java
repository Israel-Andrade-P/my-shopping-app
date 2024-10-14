package com.zel92.payment.dto;

import java.math.BigDecimal;

public record PaymentRequest(String method, BigDecimal amount, String name, String email) {
}
