package com.zel92.payment.controller;

import com.zel92.payment.dto.PaymentRequest;
import com.zel92.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService service;

    @PostMapping("/persist")
    public void persistTransaction(@RequestBody PaymentRequest request){
        service.makeTransaction(request);
    }
}
