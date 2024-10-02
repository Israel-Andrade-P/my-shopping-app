package com.zel92.product.client;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

public interface InventoryClient {

    @GetExchange("/api/v1/inventories/persist")
    void persist(@RequestParam(name = "product-id") String productId,
                 @RequestParam(name = "quantity") Integer quantity);
}
