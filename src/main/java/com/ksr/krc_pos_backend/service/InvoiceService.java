package com.ksr.krc_pos_backend.service;

import com.ksr.krc_pos_backend.dto.InvoiceSummaryDto;
import com.ksr.krc_pos_backend.model.Invoice;
import com.ksr.krc_pos_backend.model.Order;
import com.ksr.krc_pos_backend.model.enums.InvoiceStatus;
import com.ksr.krc_pos_backend.model.enums.OrderStatus;
import com.ksr.krc_pos_backend.repo.InvoiceRepo;
import com.ksr.krc_pos_backend.repo.OrderRepo;
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

    @Transactional
    public void createInvoice(UUID orderUuid, Double discount) {
        //1. find order
        Order order = orderRepo.findByUuid(orderUuid)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        //2. calculate total
        Double total = order.getOrderItems().stream()
                .mapToDouble(item -> item.getUnitPrice() * item.getQuantity())
                .sum();

        //3. Calculate net total
        Double netTotal = total - discount;

        //4. generate inv no
        long count = invoiceRepo.count() + 1;
        String invNumber = String.format("INV-%04d", count);

        Invoice invoice = Invoice.builder().
                invNumber(invNumber)
                .customer(order.getCustomer())
                .order(order)
                .total(total)
                .discount(discount)
                .netTotal(netTotal)
                .build();

        invoiceRepo.save(invoice);
        order.setStatus(OrderStatus.COMPLETED);
        order.setInvoice(invoice);
        orderRepo.save(order);
    }

    public List<InvoiceSummaryDto> getAllInvoices(String invNo, String phone, InvoiceStatus status) {
        String formattedInvNo = invNo != null ? "INV-" + invNo : null;
        return invoiceRepo.findByFilters(formattedInvNo, phone, status)
                .stream()
                .map(invoice -> InvoiceSummaryDto.builder()
                        .uuid(invoice.getUuid())
                        .invNo(invoice.getInvNumber())
                        .customer(invoice.getCustomer().getName())
                        .netTotal(invoice.getNetTotal())
                        .status(invoice.getStatus())
                        .createdAt(invoice.getCreatedAt())
                        .updatedAt(invoice.getUpdatedAt())
                        .build())
                .toList();
    }
}
