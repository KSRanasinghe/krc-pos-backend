package com.ksr.krc_pos_backend.dto;

import com.ksr.krc_pos_backend.model.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentReqDto {
    private UUID invUuid;
    private Double amount;
    private PaymentMethod paymentMethod;
}
