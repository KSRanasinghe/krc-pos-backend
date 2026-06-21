package com.ksr.krc_pos_backend.dto;

import com.ksr.krc_pos_backend.model.Customer;
import com.ksr.krc_pos_backend.model.enums.InvoiceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceSummaryDto {
    private UUID uuid;
    private String invNo;
    private String customer;
    private Double netTotal;
    private InvoiceStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
