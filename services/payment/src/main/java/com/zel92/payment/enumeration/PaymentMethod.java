package com.zel92.payment.enumeration;

import lombok.Getter;

@Getter
public enum PaymentMethod {
    CASH("Cash"),
    CREDIT_CARD("Credit card"),
    DEBIT_CARD("Debit card"),
    MASTER_CARD("Master card"),
    PIX("Pix"),
    BITCOIN("Bitcoin");

    private final String method;

    PaymentMethod(String method) {
        this.method = method;
    }
}
