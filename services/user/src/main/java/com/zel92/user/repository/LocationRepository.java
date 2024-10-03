package com.zel92.user.repository;

import com.zel92.user.entity.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<LocationEntity, Long> {
    @Query(
            """
                    SELECT l FROM LocationEntity l WHERE l.user.id=:userId
                    """
    )
    Optional<LocationEntity> findByUserId(Long userId);
}
