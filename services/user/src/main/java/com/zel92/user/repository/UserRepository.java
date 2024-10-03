package com.zel92.user.repository;

import com.zel92.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Query(
            """
                    SELECT u FROM UserEntity u WHERE u.email=:email      \s
                            """
    )
    Optional<UserEntity> findByEmail(String email);

    @Query(
            """
                    SELECT u FROM UserEntity u WHERE u.userId=:userId              \s
                            """
    )
    Optional<UserEntity> findByUserId(String userId);
}
