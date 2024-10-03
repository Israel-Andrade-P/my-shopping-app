package com.zel92.product.repository;

import com.zel92.product.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    @Query(
            """
                    SELECT c FROM CategoryEntity c WHERE c.type=:type
                    """
    )
    Optional<CategoryEntity> findByType(String type);
}
