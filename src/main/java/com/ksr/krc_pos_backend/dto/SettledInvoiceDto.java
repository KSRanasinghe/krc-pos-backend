package com.ksr.krc_pos_backend.dto;

import com.ksr.krc_pos_backend.model.enums.InvoiceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SettledInvoiceDto {
    private String invNo;
    private Double amountApplied;
    private InvoiceStatus status;
}
