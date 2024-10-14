package com.zel92.payment.service;

import com.zel92.payment.dto.PaymentRequest;

public interface PaymentService {

    void makeTransaction(PaymentRequest request);
}
