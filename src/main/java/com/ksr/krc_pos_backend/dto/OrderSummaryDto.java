package com.ksr.krc_pos_backend.dto;

import com.ksr.krc_pos_backend.model.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderSummaryDto {
    private UUID uuid;
    private OrderStatus status;
    private CustomerDto customer;
    private String invoiceNo;
    private LocalDateTime createdAt;
}
