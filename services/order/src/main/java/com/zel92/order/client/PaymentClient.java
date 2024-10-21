package com.zel92.order.client;

import com.zel92.order.dto.PaymentDTO;
import com.zel92.order.exception.PaymentServiceDownException;
import com.zel92.order.exception.ServerDownException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface PaymentClient {

    @PostExchange("/api/v1/payments/persist")
    @CircuitBreaker(name = "orderCircuitBreaker", fallbackMethod = "fallback")
    @Retry(name = "orderRetry", fallbackMethod = "fallback")
    void persistTransaction(@RequestBody PaymentDTO dto);

    default void fallback(PaymentDTO dto, Throwable throwable){
        throw new ServerDownException("Server is currently unavailable. So you are receiving this as a fallback message");
    }
}
