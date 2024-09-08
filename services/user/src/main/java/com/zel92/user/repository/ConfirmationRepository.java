package com.zel92.user.repository;

import com.zel92.user.entity.ConfirmationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ConfirmationRepository extends JpaRepository<ConfirmationEntity, Long> {
    @Query(
            """
                    SELECT c FROM ConfirmationEntity c WHERE c.key=:key
                    """
    )
    Optional<ConfirmationEntity> findByKey(String key);
}
