package com.zel92.order.service;

import com.zel92.order.dto.OrderRequest;

public interface OrderService {

    void placeOrder(OrderRequest request);
}
