package com.zel92.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserEntity extends BaseEntity{
    @Column(name = "user_id", nullable = false, updatable = false, unique = true)
    private String userId;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @NotNull
    @Column(name = "email", unique = true)
    private String email;
    @Column(name = "dob")
    private LocalDate dob;
    @Column(name = "telephone")
    private String telephone;
    private Integer loginAttempts;
    private LocalDateTime lastLogin;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean enabled;
    @OneToOne
    private LocationEntity location;
    @ManyToOne(targetEntity = RoleEntity.class)
    @JoinColumn(name = "role", referencedColumnName = "name")
    private RoleEntity role;
}
