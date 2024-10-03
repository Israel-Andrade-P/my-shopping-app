package com.zel92.order.service.impl;

import com.zel92.order.dto.OrderRequest;
import com.zel92.order.dto.OrderedItemRequest;
import com.zel92.order.repository.OrderRepository;
import com.zel92.order.repository.OrderedItemRepository;
import com.zel92.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderedItemRepository itemRepository;

    @Override
    public void placeOrder(OrderRequest request) {
        List<OrderedItemRequest> sorted = request.orderedItems().stream().sorted(Comparator.comparing(OrderedItemRequest::productId)).toList();
        List<String> productIds = sorted.stream().map(OrderedItemRequest::productId).toList();
        List<Integer> quantities = sorted.stream().map(OrderedItemRequest::quantity).toList();
    }
}
