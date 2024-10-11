package com.zel92.inventory.service.impl;

import com.zel92.inventory.dto.OrderDTO;
import com.zel92.inventory.dto.ProductCheck;
import com.zel92.inventory.entity.InventoryEntity;
import com.zel92.inventory.exception.ProductNotFoundException;
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
    public ProductCheck checkStock(OrderDTO orderDTO) {
        List<InventoryEntity> inventories = repository.findAllByProductIdInOrderByProductId(orderDTO.productIds());
        if (inventories.size() != orderDTO.productIds().size()) throw new ProductNotFoundException("Product doesn't exist");

        return checkAvailability.apply(inventories, orderDTO);
    }

    private final BiFunction<List<InventoryEntity>, OrderDTO, ProductCheck> checkAvailability = (inventories, orderDTO) -> {
        var shoppingCart = new ArrayList<>();
        var productCheck = new ProductCheck();
        for (int i = 0; i < orderDTO.productIds().size(); i++){
            var inventory = inventories.get(i);
            var quantity = orderDTO.quantities().get(i);
            if (inventory.getQuantity() >= quantity) {
                shoppingCart.add(inventory.getProductId());
            }else {
                productCheck.getProductIds().add(inventory.getProductId());
            }
        }
        productCheck.setIsInStock(shoppingCart.size() == orderDTO.productIds().size());
        return productCheck;
    };

    private final BiFunction<String, Integer, InventoryEntity> buildInventoryEntity = (productId, quantity) ->
            InventoryEntity.builder()
                    .productId(productId)
                    .quantity(quantity)
                    .build();
}
