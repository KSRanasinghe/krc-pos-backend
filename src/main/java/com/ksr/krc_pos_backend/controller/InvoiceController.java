package com.ksr.krc_pos_backend.controller;

import com.ksr.krc_pos_backend.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    @PostMapping("/{orderUuid}")
    public ResponseEntity<Void> createInvoice(
            @PathVariable UUID orderUuid, @RequestParam(defaultValue = "0") Double discount) {
        invoiceService.createInvoice(orderUuid, discount);
        return ResponseEntity.noContent().build();
    }
}
