package com.zel92.order.exception;

public class PaymentServiceDownException extends RuntimeException {
    public PaymentServiceDownException(String message) {
        super(message);
    }
}
