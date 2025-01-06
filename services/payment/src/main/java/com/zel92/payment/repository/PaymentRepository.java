package com.zel92.payment.repository;

import com.zel92.payment.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
    @Query("""
            SELECT p FROM PaymentEntity p WHERE p.userEmail=:userEmail
            """)
    List<PaymentEntity> findByUserEmail(String userEmail);
}
