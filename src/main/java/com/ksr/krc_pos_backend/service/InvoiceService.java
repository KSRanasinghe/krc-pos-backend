package com.ksr.krc_pos_backend.service;

import com.ksr.krc_pos_backend.dto.InvoiceSummaryDto;
import com.ksr.krc_pos_backend.model.*;
import com.ksr.krc_pos_backend.model.enums.InvoiceStatus;
import com.ksr.krc_pos_backend.model.enums.OrderStatus;
import com.ksr.krc_pos_backend.model.enums.PaymentMethod;
import com.ksr.krc_pos_backend.repo.*;
import com.ksr.krc_pos_backend.util.PdfResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepo invoiceRepo;
    private final OrderRepo orderRepo;
    private final OrderItemRepo orderItemRepo;
    private final CustomerRepo customerRepo;
    private final PaymentRepo paymentRepo;
    private final PaymentAllocationRepo allocationRepo;
    private final AllocationService allocationService;
    private final PdfService pdfService;

    @Transactional(rollbackFor = Exception.class)
    public PdfResponse createInvoice(UUID orderUuid, Double discount) throws Exception {
        //1. find order
        Order order = orderRepo.findByUuid(orderUuid)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getInvoice() != null) {
            throw new RuntimeException("Invoice already exists for this order");
        }
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new RuntimeException("Cannot invoice a cancelled order");
        }

        //2. calculate total
        Double total = order.getOrderItems().stream()
                .mapToDouble(item -> item.getUnitPrice() * item.getQuantity())
                .sum();

        //3. Calculate net total
        Double netTotal = total - discount;

        //4. generate inv no
        long count = invoiceRepo.count() + 1;
        String invNumber = String.format("INV-%04d", count);

        Invoice invoice = invoiceRepo.save(
                Invoice.builder().
                        invNumber(invNumber)
                        .customer(order.getCustomer())
                        .order(order)
                        .total(total)
                        .discount(discount)
                        .netTotal(netTotal)
                        .build()
        );
        order.setStatus(OrderStatus.COMPLETED);
        order.setInvoice(invoice);
        orderRepo.save(order);

        //5. find customer
        Customer customer = customerRepo
                .findByPhoneForUpdate(invoice.getCustomer().getPhone())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        //6. check wallet
        double walletBalance = customer.getWalletBalance();
        if (walletBalance > 0.001) {
            //6a. get the amount to use
            double walletUsed = Math.min(walletBalance, invoice.getNetTotal());

            //6b. make a payment
            Payment payment = paymentRepo.save(
                    Payment.builder()
                            .customer(customer)
                            .amount(walletUsed)
                            .method(PaymentMethod.WALLET_DEDUCTION)
                            .build()
            );

            //6c. call allocation service
            walletUsed = allocationService
                    .applyToInvoice(payment, invoice, walletUsed);

            //6d. update wallet balance
            walletBalance = walletBalance - walletUsed;

            //6e. save customer
            customer.setWalletBalance(walletBalance);
            customerRepo.save(customer);
        }

        //7. return pdf response
        return getInvoicePdf(invoice.getUuid());
    }

    private InvoiceSummaryDto mapToSummaryDto(Invoice invoice) {
        return InvoiceSummaryDto.builder()
                .uuid(invoice.getUuid())
                .invNo(invoice.getInvNumber())
                .orderNo(invoice.getOrder().getOrderNo())
                .customer(invoice.getCustomer().getName())
                .netTotal(invoice.getNetTotal())
                .paid(allocationRepo.sumAllocatedByInvoice(invoice))
                .status(invoice.getStatus())
                .createdAt(invoice.getCreatedAt())
                .updatedAt(invoice.getUpdatedAt())
                .build();
    }

    public List<InvoiceSummaryDto> getAllInvoices(String invNo, String phone, InvoiceStatus status) {
        String formattedInvNo = invNo != null ? "INV-" + invNo : null;
        return invoiceRepo.findByFilters(formattedInvNo, phone, status)
                .stream()
                .map(this::mapToSummaryDto)
                .toList();
    }

    public PdfResponse getInvoicePdf(UUID uuid) throws Exception {
        Invoice invoice = invoiceRepo.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        List<OrderItem> orderItems = orderItemRepo.findByOrder(invoice.getOrder());

        Double paid = allocationRepo.sumAllocatedByInvoice(invoice);
        Double balanceDue = invoice.getNetTotal() - paid;

        return pdfService.generateInvoicePdf(invoice, orderItems, paid, balanceDue);
    }

    public InvoiceSummaryDto getInvoice(UUID uuid) throws Exception {
        Invoice invoice = invoiceRepo.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        return mapToSummaryDto(invoice);
    }
}
