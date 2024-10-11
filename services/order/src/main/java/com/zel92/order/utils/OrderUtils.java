package com.zel92.order.utils;

import com.zel92.order.entity.OrderEntity;
import com.zel92.order.entity.OrderedItem;

import java.math.BigDecimal;

public class OrderUtils {

    public static OrderEntity buildOrderEntity(BigDecimal totalAmount){
        return new OrderEntity("John Doe", totalAmount);
    }

    public static OrderedItem buildOrderedItem(String productId, OrderEntity order, BigDecimal price, Integer quantity) {
        return OrderedItem.builder()
                .productId(productId)
                .price(price)
                .quantity(quantity)
                .order(order)
                .build();
    }
}
