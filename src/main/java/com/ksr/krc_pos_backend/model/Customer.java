package com.ksr.krc_pos_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "customers")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true,  nullable = false, updatable = false)
    @Builder.Default
    private UUID uuid =  UUID.randomUUID();

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String phone;

    private String note;

    @Builder.Default
    private boolean isRegular = false;

    @CreationTimestamp
    @Column(nullable = false, unique = true)
    private LocalDateTime createdAt;
}
