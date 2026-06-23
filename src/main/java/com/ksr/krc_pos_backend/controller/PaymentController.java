package com.ksr.krc_pos_backend.controller;

import com.ksr.krc_pos_backend.dto.PaymentReqDto;
import com.ksr.krc_pos_backend.dto.PaymentResDto;
import com.ksr.krc_pos_backend.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService payService;

    @GetMapping("/")
    public ResponseEntity<List<PaymentResDto>> getAllPayments(@RequestParam(required = false) String invNo) {
        return ResponseEntity.ok(payService.getAllPayments(invNo));
    }

    @PostMapping("/")
    public ResponseEntity<PaymentResDto> createPayment(@RequestBody PaymentReqDto paymentReqDto) {
        return ResponseEntity.ok(payService.createPayment(paymentReqDto));
    }
}
