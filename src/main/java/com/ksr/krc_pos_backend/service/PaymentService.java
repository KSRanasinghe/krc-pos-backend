package com.ksr.krc_pos_backend.service;

import com.ksr.krc_pos_backend.dto.PaymentReqDto;
import com.ksr.krc_pos_backend.dto.PaymentResDto;
import com.ksr.krc_pos_backend.dto.SettledInvoiceDto;
import com.ksr.krc_pos_backend.model.Customer;
import com.ksr.krc_pos_backend.model.Invoice;
import com.ksr.krc_pos_backend.model.Payment;
import com.ksr.krc_pos_backend.model.PaymentAllocation;
import com.ksr.krc_pos_backend.model.enums.InvoiceStatus;
import com.ksr.krc_pos_backend.model.enums.PaymentMethod;
import com.ksr.krc_pos_backend.repo.CustomerRepo;
import com.ksr.krc_pos_backend.repo.InvoiceRepo;
import com.ksr.krc_pos_backend.repo.PaymentAllocationRepo;
import com.ksr.krc_pos_backend.repo.PaymentRepo;
import com.ksr.krc_pos_backend.util.PdfResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final CustomerRepo customerRepo;
    private final PaymentRepo paymentRepo;
    private final InvoiceRepo invoiceRepo;
    private final AllocationService allocationService;
    private final PaymentAllocationRepo allocationRepo;
    private final PdfService pdfService;

    @Transactional(rollbackFor = Exception.class)
    public PdfResponse createPayment(PaymentReqDto paymentReqDto) throws Exception {
        // 1. Check the request: amount > 0, method is not WALLET_DEDUCTION
        //    (if wrong, throw RuntimeException)
        if (paymentReqDto.getAmount() <= 0 || paymentReqDto.getMethod() == PaymentMethod.WALLET_DEDUCTION) {
            throw new RuntimeException("Invalid Payment Request");
        }

        // 2. Find and lock the customer by phone (findByPhoneForUpdate)
        //    (if not found, throw RuntimeException)
        Customer customer = customerRepo.findByPhoneForUpdate(paymentReqDto.getPhone())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // 3. Check the customer has some due (calculateTotalDue > 0)
        //    (if not, throw RuntimeException "No due amount")
        Double totalDue = allocationService.calculateTotalDue(customer);
        if (totalDue <= 0.001) {
            throw new RuntimeException("No due amount");
        }

        // 4. Build and save the Payment (customer, amount, method)
        Payment newPayment = paymentRepo.save(Payment.builder()
                .customer(customer)
                .amount(paymentReqDto.getAmount())
                .method(paymentReqDto.getMethod())
                .build());

        // 5. moneyLeft = req.getAmount()
        double moneyLeft = paymentReqDto.getAmount();

        // 6. Get the unpaid invoices (oldest first)
        List<Invoice> unpaidInvoices = invoiceRepo
                .findByCustomerAndStatusInOrderByCreatedAtAsc(
                        customer,
                        List.of(InvoiceStatus.UNPAID, InvoiceStatus.PARTIAL)
                );

        // 7. Loop over the invoices:
        //      - if moneyLeft is almost 0, break
        //      - call allocationService.applyToInvoice(payment, invoice, moneyLeft)
        //      - subtract the result from moneyLeft
        for (Invoice invoice : unpaidInvoices) {
            if (moneyLeft <= 0.001) {
                break;
            }

            Double amountUsed = allocationService
                    .applyToInvoice(newPayment, invoice, moneyLeft);
            moneyLeft = moneyLeft - amountUsed;
        }

        // 8. If moneyLeft > 0, add it to customer.walletBalance and save the customer
        if (moneyLeft > 0.001) {
            customer.setWalletBalance(customer.getWalletBalance() + moneyLeft);
            customerRepo.save(customer);
        }

        return getPaymentPdf(newPayment.getUuid());
    }

    public List<PaymentResDto> getAllPayments(String phone) {
        List<Payment> payments = paymentRepo
                .findByCustomerPhoneContaining(phone != null ? phone : "");

        return payments.stream()
                .map(this::mapToDto)
                .toList();
    }

    private PaymentResDto mapToDto(Payment payment) {
        // 1. get this payment's allocation rows
        List<PaymentAllocation> allocations =
                allocationRepo.findByPayment(payment);

        // 2. turn each allocation row into one SettledInvoiceDto
        List<SettledInvoiceDto> settledInvoices = new ArrayList<>();

        for (PaymentAllocation allocation : allocations) {
            settledInvoices.add(SettledInvoiceDto.builder()
                    .amountApplied(allocation.getAllocatedAmount())
                    .invNo(allocation.getInvoice().getInvNumber())
                    .status(allocation.getInvoice().getStatus())
                    .build());
        }

        // 3. build and return one PaymentResDto
        return PaymentResDto.builder()
                .uuid(payment.getUuid())
                .phone(payment.getCustomer().getPhone())
                .customerName(payment.getCustomer().getName())
                .amount(payment.getAmount())
                .method(payment.getMethod())
                .paidAt(payment.getPaidAt())
                .walletBalance(payment.getCustomer().getWalletBalance())
                .settledInvoices(settledInvoices)
                .build();
    }

    public PdfResponse getPaymentPdf(UUID uuid) throws Exception{
        Payment payment = paymentRepo.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        PaymentResDto dto = mapToDto(payment);

        return pdfService.generatePaymentPdf(dto, payment.getId());
    }
}
