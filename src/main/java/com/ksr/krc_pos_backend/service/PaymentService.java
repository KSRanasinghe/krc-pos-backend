package com.ksr.krc_pos_backend.service;

import com.ksr.krc_pos_backend.dto.PaymentReqDto;
import com.ksr.krc_pos_backend.dto.PaymentResDto;
import com.ksr.krc_pos_backend.model.Invoice;
import com.ksr.krc_pos_backend.model.Payment;
import com.ksr.krc_pos_backend.model.enums.InvoiceStatus;
import com.ksr.krc_pos_backend.repo.InvoiceRepo;
import com.ksr.krc_pos_backend.repo.PaymentRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepo paymentRepo;
    private final InvoiceRepo invoiceRepo;

    @Transactional
    public PaymentResDto createPayment(PaymentReqDto paymentReqDto) {
        Invoice invoice = invoiceRepo.findByUuid(paymentReqDto.getInvUuid())
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        if (invoice.getStatus() == InvoiceStatus.PAID) {
            throw new RuntimeException("Invoice is already paid in full");
        }

        double totalPaid = paymentRepo.sumByInvoice(invoice) + paymentReqDto.getAmount();

        if (totalPaid >= invoice.getNetTotal()) {
            invoice.setStatus(InvoiceStatus.PAID);
        } else {
            invoice.setStatus(InvoiceStatus.PARTIAL);
        }

        Payment payment = Payment.builder()
                .amount(paymentReqDto.getAmount())
                .method(paymentReqDto.getPaymentMethod())
                .invoice(invoice)
                .build();

        Payment savedPayment = paymentRepo.save(payment);
        invoiceRepo.save(invoice);

        return PaymentResDto.builder()
                .uuid(savedPayment.getUuid())
                .invNo(savedPayment.getInvoice().getInvNumber())
                .amount(savedPayment.getAmount())
                .method(savedPayment.getMethod())
                .paidAt(savedPayment.getPaidAt())
                .build();
    }

    public List<PaymentResDto> getAllPayments(String invNo) {
        String formattedInvNo = invNo != null ? "INV-" + invNo : null;

        return paymentRepo.findByFilters(formattedInvNo)
                .stream()
                .map(payment -> PaymentResDto.builder()
                        .uuid(payment.getUuid())
                        .invNo(payment.getInvoice().getInvNumber())
                        .amount(payment.getAmount())
                        .method(payment.getMethod())
                        .paidAt(payment.getPaidAt())
                        .build())
                .toList();
    }
}
