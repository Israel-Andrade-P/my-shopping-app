package com.zel92.user.entity;

import com.zel92.user.enumeration.Authority;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "roles")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RoleEntity extends BaseEntity{
    @Column(name = "name", unique = true)
    private String name;
    private Authority authority;
}
