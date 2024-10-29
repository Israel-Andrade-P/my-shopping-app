package com.zel92.order.service;

import com.zel92.order.dto.OrderRequest;
import com.zel92.order.dto.OrderResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface OrderService {

    void placeOrder(OrderRequest orderReq, HttpServletRequest request);

    List<OrderResponse> findByUserId(String userId, HttpServletRequest request);

    OrderResponse findByOrderId(String orderId, HttpServletRequest request);
}
