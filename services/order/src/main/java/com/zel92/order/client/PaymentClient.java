package com.zel92.order.client;

import com.zel92.order.dto.PaymentDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface PaymentClient {

    @PostExchange("/api/v1/payments/persist")
    void persistTransaction(@RequestBody PaymentDTO dto);
}
