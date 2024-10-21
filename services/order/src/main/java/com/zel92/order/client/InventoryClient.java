package com.zel92.order.client;

import com.zel92.order.dto.OrderDTO;
import com.zel92.order.dto.ProductCheck;
import com.zel92.order.exception.ServerDownException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;

public interface InventoryClient {
    @PostExchange("/api/v1/inventories/stock")
    @CircuitBreaker(name = "orderCircuitBreaker", fallbackMethod = "fallback")
    @Retry(name = "orderRetry", fallbackMethod = "fallback")
    ProductCheck checkStock(@RequestBody OrderDTO orderDTO);


    default ProductCheck fallback(OrderDTO orderDTO, Throwable throwable){
        System.out.println("Trying to reach Inventory Service");
        throw new ServerDownException("Server is currently unavailable. So you are receiving this as a fallback message");
//        return new ProductCheck(List.of("An server error has occurred. You are receiving this as a fallback message"), false);
    }
}
