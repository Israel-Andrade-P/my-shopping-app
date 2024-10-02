package com.zel92.inventory.service.impl;

import com.zel92.inventory.entity.InventoryEntity;
import com.zel92.inventory.repository.InventoryRepository;
import com.zel92.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.BiFunction;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepository repository;
    @Override
    public void persistInventory(String productId, Integer quantity) {
        repository.save(buildInventoryEntity.apply(productId, quantity));
    }

    @Override
    public Boolean checkStock(List<String> productIds, List<Integer> quantities) {
        return null;
    }

    private final BiFunction<String, Integer, InventoryEntity> buildInventoryEntity = (productId, quantity) ->
             InventoryEntity.builder()
                     .productId(productId)
                     .quantity(quantity)
                     .build();
}
