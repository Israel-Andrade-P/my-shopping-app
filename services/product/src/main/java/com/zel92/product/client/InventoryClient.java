package com.zel92.product.client;

import com.zel92.product.exception.ServerDownException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

public interface InventoryClient {

    @GetExchange("/api/v1/inventories/persist")
    @CircuitBreaker(name = "productCircuitBreaker", fallbackMethod = "fallback")
    @Retry(name = "orderRetry", fallbackMethod = "fallback")
    void persist(@RequestParam(name = "product-id") String productId,
                 @RequestParam(name = "quantity") Integer quantity);

    default void fallback(String productId, Integer quantity, Throwable throwable) {
        System.out.println("Trying to reach Inventory Service");
        throw new ServerDownException("Server is currently unavailable. So you are receiving this as a fallback message");
    }
}
