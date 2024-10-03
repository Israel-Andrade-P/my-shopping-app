package com.zel92.inventory.service.impl;

import com.zel92.inventory.entity.InventoryEntity;
import com.zel92.inventory.exception.ProductNotFoundException;
import com.zel92.inventory.exception.ProductOutOfStockException;
import com.zel92.inventory.repository.InventoryRepository;
import com.zel92.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        List<InventoryEntity> inventories = repository.findAllByProductIdInOrderByProductId(productIds);
        if (inventories.size() != productIds.size()) throw new ProductNotFoundException("Product doesn't exist");
        var shoppingCart = new ArrayList<>();
        for (int i = 0; i < productIds.size(); i++){
            var inventory = inventories.get(i);
            var quantity = quantities.get(i);
            if (inventory.getQuantity() < quantity) throw new ProductOutOfStockException("Product with ID: " + inventory.getProductId() + " is not in stock");
            shoppingCart.add(inventory.getProductId());
        }
        return shoppingCart.size() == inventories.size();
    }

    private final BiFunction<String, Integer, InventoryEntity> buildInventoryEntity = (productId, quantity) ->
            InventoryEntity.builder()
                    .productId(productId)
                    .quantity(quantity)
                    .build();
}
