package com.zel92.product.service;

import com.zel92.product.dto.OrderDTO;
import com.zel92.product.dto.request.ProductRequest;
import com.zel92.product.dto.response.ProductResponse;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    void createProduct(ProductRequest product);
    ProductResponse findByProductId(String productId);
    void deleteProduct(String productId);
    List<BigDecimal> getPrice(OrderDTO orderDTO);
}
