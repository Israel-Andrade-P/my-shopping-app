package com.zel92.inventory.repository;

import com.zel92.inventory.entity.InventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InventoryRepository extends JpaRepository<InventoryEntity, Long> {

//    @Query(
//            """
//                    SELECT i FROM InventoryEntity i WHERE i.productId=:
//                    """
//    )
    List<InventoryEntity> findAllByProductIdInOrderByProductId(List<String> productIds);
}
