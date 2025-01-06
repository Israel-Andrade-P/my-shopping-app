package com.zel92.order.client;

import com.zel92.order.dto.UserResponse;
import com.zel92.order.exception.ServerDownException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

public interface UserClient {
    @GetExchange("/api/v1/auth/retrieve")
    @CircuitBreaker(name = "orderCircuitBreaker", fallbackMethod = "fallback")
    @Retry(name = "orderRetry", fallbackMethod = "fallback")
    UserResponse retrieveUser(@RequestParam(name = "token") String token);

    default UserResponse fallback(String token, Throwable throwable){
        throw new ServerDownException("Server is currently unavailable. So you are receiving this as a fallback message");
//        return new UserResponse("Server is currently unavailable. You are getting this message as a fallback", null, null, null, null, null, null);
    }
}
