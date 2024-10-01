package com.zel92.product.repository;

import com.zel92.product.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    @Query(
            """
                    SELECT p FROM ProductEntity p WHERE p.productId=:productId
                    """
    )
    Optional<ProductEntity> findByProductId(String productId);
}
