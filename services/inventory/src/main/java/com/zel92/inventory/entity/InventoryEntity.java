package com.zel92.inventory.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "inventories")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class InventoryEntity {
    @Id
    @SequenceGenerator(name = "inventories_id_seq", sequenceName = "inventories_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "inventories_id_seq")
    private Long id;
    private String productId;
    private Integer quantity;
}
