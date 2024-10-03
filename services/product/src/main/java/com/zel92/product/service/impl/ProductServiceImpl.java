package com.zel92.product.service.impl;

import com.zel92.product.client.InventoryClient;
import com.zel92.product.dto.request.ProductRequest;
import com.zel92.product.dto.response.ProductResponse;
import com.zel92.product.entity.CategoryEntity;
import com.zel92.product.entity.ProductEntity;
import com.zel92.product.exception.CategoryNotFoundException;
import com.zel92.product.exception.ProductNotFoundException;
import com.zel92.product.repository.CategoryRepository;
import com.zel92.product.repository.ProductRepository;
import com.zel92.product.service.ProductService;
import com.zel92.product.utils.ProductUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final InventoryClient inventoryClient;

    @Override
    public void createProduct(ProductRequest product) {
        ProductEntity productEntity = productRepository.save(createNewProductEntity(product));
        inventoryClient.persist(productEntity.getProductId(), product.quantity());
    }

    @Override
    public ProductResponse findByProductId(String productId) {
        ProductEntity productEntity = getProductEntityById(productId);
        return ProductUtils.fromProductEntity(productEntity);
    }

    @Override
    public void deleteProduct(String productId) {
        var product = getProductEntityById(productId);
        productRepository.delete(product);
    }

    private ProductEntity getProductEntityById(String productId) {
        return productRepository
                .findByProductId(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product with ID: " + productId + " not found"));
    }

    private ProductEntity createNewProductEntity(ProductRequest product) {
        CategoryEntity category = getCategoryByType(product.category());
        return ProductUtils.buildProductEntity(product, category);
    }

    private CategoryEntity getCategoryByType(String type) {
        return categoryRepository.findByType(type).orElseThrow(() -> new CategoryNotFoundException("Category not found"));
    }
}
