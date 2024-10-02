package com.zel92.inventory.service;

import java.util.List;

public interface InventoryService {

    void persistInventory(String productId, Integer quantity);
    Boolean checkStock(List<String> productIds, List<Integer> quantities);
}
