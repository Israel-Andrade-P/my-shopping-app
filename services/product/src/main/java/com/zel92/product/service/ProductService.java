package com.zel92.product.service;

import com.zel92.product.dto.request.ProductRequest;
import com.zel92.product.dto.response.ProductResponse;

public interface ProductService {

    void createProduct(ProductRequest product);
    ProductResponse findByProductId(String productId);
    void deleteProduct(String productId);
}
