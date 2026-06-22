package com.ksr.krc_pos_backend.controller;

import com.ksr.krc_pos_backend.dto.PaymentReqDto;
import com.ksr.krc_pos_backend.dto.PaymentResDto;
import com.ksr.krc_pos_backend.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService payService;

    @PostMapping("/")
    public ResponseEntity<PaymentResDto> createPayment(@RequestBody PaymentReqDto paymentReqDto) {
        return ResponseEntity.ok(payService.createPayment(paymentReqDto));
    }
}
