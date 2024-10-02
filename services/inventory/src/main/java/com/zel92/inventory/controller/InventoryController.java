package com.zel92.inventory.controller;

import com.zel92.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/inventories")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService service;

    @GetMapping("/persist")
    public void persist(@RequestParam(name = "product-id") String productId,
                        @RequestParam(name = "quantity") Integer quantity){
        service.persistInventory(productId, quantity);
    }
}
