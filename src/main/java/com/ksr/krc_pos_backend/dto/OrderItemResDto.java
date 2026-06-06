package com.ksr.krc_pos_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemResDto {
    private UUID uuid;
    private Integer quantity;
    private Double unitPrice;
    private String taskName;
    private String variantLabel;
}
