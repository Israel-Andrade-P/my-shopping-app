package com.zel92.payment.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(name = "payment_method", nullable = false)
    private String paymentMethod;
    @Column(name = "paid_amount", nullable = false)
    private BigDecimal paidAmount;
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "payer_name", nullable = false)
    private String payerName;
    @Column(name = "user_email", nullable = false)
    private String userEmail;
}
