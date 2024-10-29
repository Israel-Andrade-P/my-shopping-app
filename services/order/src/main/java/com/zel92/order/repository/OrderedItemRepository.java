package com.zel92.order.repository;

import com.zel92.order.entity.OrderEntity;
import com.zel92.order.entity.OrderedItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderedItemRepository extends JpaRepository<OrderedItem, Long> {

    @Query("""
            SELECT o FROM OrderedItem o WHERE o.order=:order
            """)
    List<OrderedItem> findByOrder(OrderEntity order);

}
