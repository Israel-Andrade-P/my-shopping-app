package com.zel92.product.service.impl;

import com.zel92.product.client.InventoryClient;
import com.zel92.product.client.UserClient;
import com.zel92.product.dto.OrderDTO;
import com.zel92.product.dto.request.ProductRequest;
import com.zel92.product.dto.response.ProductResponse;
import com.zel92.product.dto.response.UserResponse;
import com.zel92.product.entity.CategoryEntity;
import com.zel92.product.entity.ProductEntity;
import com.zel92.product.exception.AuthorizationFailedException;
import com.zel92.product.exception.CategoryNotFoundException;
import com.zel92.product.exception.ProductNotFoundException;
import com.zel92.product.repository.CategoryRepository;
import com.zel92.product.repository.ProductRepository;
import com.zel92.product.service.ProductService;
import com.zel92.product.utils.ProductUtils;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import static com.zel92.product.utils.ProductUtils.*;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final InventoryClient inventoryClient;
    private final UserClient userClient;

    @Override
    public void createProduct(ProductRequest product, HttpServletRequest request) throws AuthorizationFailedException {
        var user = getUser(request);
        if (checkAuthStatus(user)){
            ProductEntity productEntity = productRepository.save(createNewProductEntity(product, user));
            inventoryClient.persist(productEntity.getProductId(), product.quantity());
        }else throw new AuthorizationFailedException("You don't have enough permission for that");
    }

    @Override
    public ProductResponse findByProductId(String productId) {
        ProductEntity productEntity = getProductEntityById(productId);
        return fromProductEntity(productEntity);
    }

    @Override
    public void deleteProduct(String productId, HttpServletRequest request) throws AuthorizationFailedException {
        if (checkAuthStatus(getUser(request))){
            var product = getProductEntityById(productId);
            productRepository.delete(product);
        }else throw new AuthorizationFailedException("You don't have enough permission for that");
    }
    @Override
    public List<BigDecimal> getPrice(OrderDTO orderDTO) {
        List<ProductEntity> products = productRepository.findAllByProductIdInOrderByProductId(orderDTO.productIds());
        return products.stream().map(ProductEntity::getPrice).toList();
    }

    @Override
    public List<ProductResponse> findByCategory(String category) {
        var categoryEntity = getCategoryByType(category);
        return productRepository.findAllByCategory(categoryEntity).stream().map(ProductUtils::fromProductEntity).toList();
    }

    @Override
    public void updateProduct(ProductRequest product, String productId, HttpServletRequest request) throws AuthorizationFailedException {
        if (checkAuthStatus(getUser(request))){
            var productDB = getProductEntityById(productId);

            if (StringUtils.isNotBlank(product.name())){
                productDB.setName(product.name());
            }
            if (StringUtils.isNotBlank(product.description())){
                productDB.setDescription(product.description());
            }
            if (StringUtils.isNotBlank(product.category())){
                var categoryEntity = getCategoryByType(product.category());
                productDB.setCategory(categoryEntity);
            }

            if (product.price() != null){
                productDB.setPrice(product.price());
            }

            productRepository.save(productDB);
        }else throw new AuthorizationFailedException("You don't have enough permission for that");

    }

    private ProductEntity getProductEntityById(String productId) {
        return productRepository
                .findByProductId(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product with ID: " + productId + " not found"));
    }

    private ProductEntity createNewProductEntity(ProductRequest product, UserResponse user) {
        CategoryEntity category = getCategoryByType(product.category());
        return buildProductEntity(product, category, user);
    }

    private CategoryEntity getCategoryByType(String type) {
        return categoryRepository.findByType(type).orElseThrow(() -> new CategoryNotFoundException("Category not found"));
    }

    private Boolean checkAuthStatus(UserResponse user){
        return Objects.equals(user.role(), "SUPER_ADMIN");
    }

    private UserResponse getUser(HttpServletRequest request){
        var token = request.getHeader(HttpHeaders.AUTHORIZATION).substring(7);
        return userClient.retrieveUser(token);
    }

    //Methods created for Testing Practice, please delete these methods bellow after done messing around with Unit Testing

    public ProductEntity addProduct(ProductEntity product){
        if (validateProductName(product.getName())){
            return productRepository.save(product);
        }
        throw new RuntimeException("Invalid name");
    }

    public void delete(Long id){
        productRepository.deleteById(id);
    }

    private boolean validateProductName(String name){
        return name != null && !name.isEmpty();
    }

}
