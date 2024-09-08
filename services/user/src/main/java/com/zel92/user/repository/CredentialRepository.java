package com.zel92.user.repository;

import com.zel92.user.entity.CredentialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CredentialRepository extends JpaRepository<CredentialEntity, Long> {
    @Query(
            """
                    SELECT c FROM CredentialEntity c WHERE c.user.id=:userId
                    """
    )
    Optional<CredentialEntity> findByUserId(Long userId);
}
