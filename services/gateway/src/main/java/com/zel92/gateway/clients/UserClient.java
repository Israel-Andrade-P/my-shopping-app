package com.zel92.gateway.clients;

import com.zel92.gateway.exception.ServerDownException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

public interface UserClient {
    @GetExchange("/api/v1/auth/validate")
    @CircuitBreaker(name = "gatewayCircuitBreaker", fallbackMethod = "fallback")
    @Retry(name = "gatewayRetry", fallbackMethod = "fallback")
    Boolean validateJwt(@RequestParam(name = "token") String jwt);

    default Boolean fallback(String jwt, Throwable throwable){
        throw new ServerDownException("Server is currently unavailable. So you are receiving this as a fallback message");
    }
}
