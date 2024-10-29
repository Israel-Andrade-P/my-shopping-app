package com.zel92.order.utils;

import com.zel92.order.dto.OrderResponse;
import com.zel92.order.dto.OrderedItemResponse;
import com.zel92.order.dto.UserResponse;
import com.zel92.order.entity.OrderEntity;
import com.zel92.order.entity.OrderedItem;
import com.zel92.order.repository.OrderedItemRepository;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

public class OrderUtils {

    public static OrderEntity buildOrderEntity(UserResponse user, BigDecimal totalAmount){
        return new OrderEntity(user.userId(), user.fullName(), totalAmount);
    }

    public static OrderedItem buildOrderedItem(String productId, OrderEntity order, BigDecimal price, Integer quantity) {
        return OrderedItem.builder()
                .productId(productId)
                .price(price)
                .quantity(quantity)
                .order(order)
                .build();
    }

//    public static OrderResponse toOrderResponse(OrderEntity order){
////        var items =
////        return new OrderResponse(order.getOrderId(), order.getClientName(), order.getTotalAmount(), order.)
//    }

    public static OrderedItemResponse toOrderedItemResponse(OrderedItem item){
        return new OrderedItemResponse(item.getProductId(), item.getPrice(), item.getQuantity());
    }

}
