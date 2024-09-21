package com.zel92.user.repository;

import com.zel92.user.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    @Query(
            """
                    SELECT r FROM RoleEntity r WHERE r.name=:name
                    """
    )
    Optional<RoleEntity> findByRoleName(String name);
}
