package com.ksr.krc_pos_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskVariantDto {
    private UUID uuid;
    private String label;
    private Double price;
    private UUID taskUuid;
    private Boolean isActive;
}
