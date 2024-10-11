package com.zel92.order.client;

import com.zel92.order.dto.OrderDTO;
import com.zel92.order.dto.ProductCheck;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface InventoryClient {
    @PostExchange("/api/v1/inventories/stock")
    ProductCheck checkStock(@RequestBody OrderDTO orderDTO);
}
