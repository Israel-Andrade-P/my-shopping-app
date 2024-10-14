package com.zel92.order.service;

import com.zel92.order.dto.OrderRequest;
import jakarta.servlet.http.HttpServletRequest;

public interface OrderService {

    void placeOrder(OrderRequest orderReq, HttpServletRequest request);
}
