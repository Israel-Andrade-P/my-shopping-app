package com.zel92.order.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ordered_items")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EntityListeners(AuditingEntityListener.class)
public class OrderedItem {
    @Id
    @SequenceGenerator(name = "ordered_items_id_seq", sequenceName = "ordered_items_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ordered_items_id_seq")
    private Long id;
    private String productId;
    private BigDecimal price;
    private Integer quantity;
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @ManyToOne(targetEntity = OrderEntity.class)
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private OrderEntity order;
}
