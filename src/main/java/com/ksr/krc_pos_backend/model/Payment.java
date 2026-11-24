package com.ksr.krc_pos_backend.model;

import com.ksr.krc_pos_backend.model.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false, updatable = false)
    @Builder.Default
    private UUID uuid  = UUID.randomUUID();

    @Column(nullable = false, updatable = false)
    private Double amount;

    @Enumerated(EnumType.STRING)
    private PaymentMethod method;

    @ManyToOne
    @JoinColumn(name = "customer_id",  nullable = false)
    private Customer customer;

    @CreationTimestamp
    private LocalDateTime paidAt;
}
