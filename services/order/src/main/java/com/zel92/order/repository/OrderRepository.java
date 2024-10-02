package com.zel92.order.repository;

import com.zel92.order.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    @Query(
            """
                    SELECT o FROM OrderEntity o WHERE o.orderId=:orderId
                    """
    )
    Optional<OrderEntity> findByOrderId(String orderId);
}
