package com.ksr.krc_pos_backend.dto;

import com.ksr.krc_pos_backend.model.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResDto {
    private UUID uuid;
    private String phone;
    private String customerName;
    private Double amount;
    private PaymentMethod method;
    private LocalDateTime paidAt;
    private Double walletBalance;
    private List<SettledInvoiceDto> settledInvoices;
}
