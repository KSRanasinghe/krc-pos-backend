package com.ksr.krc_pos_backend.controller;

import com.ksr.krc_pos_backend.dto.PaymentReqDto;
import com.ksr.krc_pos_backend.dto.PaymentResDto;
import com.ksr.krc_pos_backend.service.PaymentService;
import com.ksr.krc_pos_backend.util.PdfResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping
    public ResponseEntity<List<PaymentResDto>> getAllPayments(
            @RequestParam(required = false) String phone) {
        return ResponseEntity.ok(paymentService.getAllPayments(phone));
    }

    @PostMapping
    public ResponseEntity<byte[]> createPayment(@RequestBody PaymentReqDto paymentReqDto)
            throws Exception {
        PdfResponse response = paymentService.createPayment(paymentReqDto);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" + response.getFileName() + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(response.getPdf());
    }
}
