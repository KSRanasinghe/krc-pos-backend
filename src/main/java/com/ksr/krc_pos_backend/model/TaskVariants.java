package com.ksr.krc_pos_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "task_variants")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskVariants {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false, updatable = false)
    @Builder.Default
    private UUID uuid = UUID.randomUUID();

    @Column(nullable = false)
    private String label;

    @Column(nullable = false)
    private Double price;

    @ManyToOne
    @JoinColumn(name = "task_id",  nullable = false)
    private DesignTask designTask;

    @Column(nullable = false)
    @Builder.Default
    private boolean isActive = true;
}
