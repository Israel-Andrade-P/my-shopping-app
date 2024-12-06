package com.zel92.payment.utils;

import com.zel92.payment.dto.PaymentRequest;
import com.zel92.payment.entity.PaymentEntity;
import com.zel92.payment.enumeration.PaymentMethod;
import com.zel92.payment.exception.InvalidPaymentMethodException;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;

public class PaymentUtils {
    public static PaymentEntity buildPaymentEntity(PaymentRequest paymentRequest) {
        return PaymentEntity.builder()
                .paymentMethod(getPaymentMethod.apply(paymentRequest.method()))
                .paidAmount(paymentRequest.amount())
                .payerName(paymentRequest.name())
                .userEmail(paymentRequest.email())
                .build();
    }

    private static final Function<String, String> getPaymentMethod = method -> {
        return Arrays.stream(PaymentMethod.values())
                .filter(payMeth -> Objects.equals(payMeth.getMethod(), method))
                .findFirst()
                .orElseThrow(() -> new InvalidPaymentMethodException("Payment method not supported")) //catch it!!
                .getMethod();
    };

}
