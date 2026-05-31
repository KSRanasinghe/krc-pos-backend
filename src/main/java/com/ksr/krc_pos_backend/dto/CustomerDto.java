package com.ksr.krc_pos_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CustomerDto {
    private UUID uuid;
    private String name;
    private String phone;
    private String note;
    private Boolean isRegular;
}
