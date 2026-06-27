package com.ksr.krc_pos_backend.util;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PdfResponse {
    private String invNo;
    private byte[] pdf;
}
