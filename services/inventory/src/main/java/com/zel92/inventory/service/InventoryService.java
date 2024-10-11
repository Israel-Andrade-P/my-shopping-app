package com.zel92.inventory.service;

import com.zel92.inventory.dto.OrderDTO;
import com.zel92.inventory.dto.ProductCheck;

public interface InventoryService {

    void persistInventory(String productId, Integer quantity);
    ProductCheck checkStock(OrderDTO inventoryCheck);
}
