package com.zel92.product.service;

import com.zel92.product.dto.OrderDTO;
import com.zel92.product.dto.request.ProductRequest;
import com.zel92.product.dto.response.ProductResponse;
import com.zel92.product.exception.AuthorizationFailedException;
import jakarta.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    void createProduct(ProductRequest product, HttpServletRequest request) throws AuthorizationFailedException;
    ProductResponse findByProductId(String productId);
    void deleteProduct(String productId, HttpServletRequest request) throws AuthorizationFailedException;
    List<BigDecimal> getPrice(OrderDTO orderDTO);

    List<ProductResponse> findByCategory(String category);

    void updateProduct(ProductRequest product, String productId, HttpServletRequest request) throws AuthorizationFailedException;
}
