package com.zel92.product.utils;

import com.zel92.product.dto.request.ProductRequest;
import com.zel92.product.dto.response.ProductResponse;
import com.zel92.product.entity.CategoryEntity;
import com.zel92.product.entity.ProductEntity;

import java.security.SecureRandom;
import java.util.function.Supplier;

public class ProductUtils {
    public static ProductEntity buildProductEntity(ProductRequest product, CategoryEntity category) {
        return ProductEntity.builder()
                .productId(supplyProductId.get())
                .name(product.name())
                .description(product.description())
                .price(product.price())
                .ownerId(null)
                .category(category)
                .build();
    }

    public static ProductResponse fromProductEntity(ProductEntity productEntity){
        return ProductResponse.builder()
                .productId(productEntity.getProductId())
                .name(productEntity.getName())
                .description(productEntity.getDescription())
                .price(productEntity.getPrice())
                .build();
    }

    private final static Supplier<String> supplyProductId = () -> {
        String pool  = "1234567890";
        StringBuilder builder = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < 6; i++){
            var randIndex = random.nextInt(pool.length());
            builder.append(pool.charAt(randIndex));
        }
        return builder.toString();
    };
}
