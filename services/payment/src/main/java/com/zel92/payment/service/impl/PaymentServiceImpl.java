package com.zel92.payment.service.impl;

import com.zel92.payment.dto.PaymentRequest;
import com.zel92.payment.repository.PaymentRepository;
import com.zel92.payment.service.PaymentService;
import com.zel92.payment.utils.PaymentUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.zel92.payment.utils.PaymentUtils.*;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository repository;
    @Override
    public void makeTransaction(PaymentRequest paymentRequest) {
        repository.save(buildPaymentEntity(paymentRequest));
        //todo async communication with notification service through Kafka
    }
}
