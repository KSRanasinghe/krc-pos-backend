package com.ksr.krc_pos_backend.controller;

import com.ksr.krc_pos_backend.dto.InvoiceSummaryDto;
import com.ksr.krc_pos_backend.model.enums.InvoiceStatus;
import com.ksr.krc_pos_backend.service.InvoiceService;
import com.ksr.krc_pos_backend.util.PdfResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    @GetMapping("/")
    public ResponseEntity<List<InvoiceSummaryDto>> getAllInvoices(
            @RequestParam(required = false) String invNo,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) InvoiceStatus status
    ) {
        return ResponseEntity.ok(invoiceService.getAllInvoices(invNo, phone, status));
    }

    @PostMapping("/{orderUuid}")
    public ResponseEntity<byte[]> createInvoice(
            @PathVariable UUID orderUuid, @RequestParam(defaultValue = "0") Double discount) throws Exception {
        PdfResponse response = invoiceService.createInvoice(orderUuid, discount);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + response.getInvNo() + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(response.getPdf());
    }

    @GetMapping("/{uuid}/pdf")
    public ResponseEntity<byte[]> getInvoicePdf(@PathVariable UUID uuid) throws Exception {
        PdfResponse response = invoiceService.getInvoicePdf(uuid);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + response.getInvNo() + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(response.getPdf());
    }
}
