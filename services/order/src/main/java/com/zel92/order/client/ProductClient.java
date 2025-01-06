package com.zel92.order.client;

import com.zel92.order.dto.OrderDTO;
import com.zel92.order.exception.ServerDownException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

import java.math.BigDecimal;
import java.util.List;

public interface ProductClient {

    @PostExchange("/api/v1/products/price")
    @CircuitBreaker(name = "orderCircuitBreaker", fallbackMethod = "fallback")
    @Retry(name = "orderRetry", fallbackMethod = "fallback")
    List<BigDecimal> getPrice(@RequestBody OrderDTO orderDTO);

    default List<BigDecimal> fallback(OrderDTO dto, Throwable throwable){
        System.out.println("Product Service is currently down");
        throw new ServerDownException("Server is currently unavailable. So you are receiving this as a fallback message");
//        return List.of(BigDecimal.valueOf(0.0));
    }
}
