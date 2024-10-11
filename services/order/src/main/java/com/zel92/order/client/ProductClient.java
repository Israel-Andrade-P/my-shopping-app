package com.zel92.order.client;

import com.zel92.order.dto.OrderDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

import java.math.BigDecimal;
import java.util.List;

public interface ProductClient {

    @PostExchange("/api/v1/products/price")
    List<BigDecimal> getPrice(@RequestBody OrderDTO orderDTO);
}
