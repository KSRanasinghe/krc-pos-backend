package com.ksr.krc_pos_backend.service;

import com.ksr.krc_pos_backend.model.Customer;
import com.ksr.krc_pos_backend.model.Invoice;
import com.ksr.krc_pos_backend.model.Payment;
import com.ksr.krc_pos_backend.model.PaymentAllocation;
import com.ksr.krc_pos_backend.model.enums.InvoiceStatus;
import com.ksr.krc_pos_backend.repo.InvoiceRepo;
import com.ksr.krc_pos_backend.repo.PaymentAllocationRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AllocationService {

    private final PaymentAllocationRepo allocationRepo;
    private final InvoiceRepo invoiceRepo;

    public Double applyToInvoice(Payment payment, Invoice invoice, Double moneyLeft) {
        // How much has been paid before?
        Double alreadyPaid = allocationRepo.sumAllocatedByInvoice(invoice);

        // How much more is needed?
        double due = invoice.getNetTotal() - alreadyPaid;

        // How much should pay now?
        Double amountUsed = Math.min(due, moneyLeft);

        // Writing a note (allocation row)
        PaymentAllocation allocation = PaymentAllocation.builder()
                .invoice(invoice)
                .payment(payment)
                .allocatedAmount(amountUsed)
                .build();
        allocationRepo.save(allocation);

        // Total payment has been made so far
        double totalPaid = alreadyPaid + amountUsed;

        // Updating the invoice table
        InvoiceStatus newStatus = InvoiceStatus.PARTIAL;
        if (totalPaid >= invoice.getNetTotal() - 0.001) {
            newStatus = InvoiceStatus.PAID;
        }
        invoice.setStatus(newStatus);
        invoiceRepo.save(invoice);

        // return the amount we used for this particular invoice
        return amountUsed;
    }

    public Double calculateTotalDue(Customer customer) {
        double totalDue = 0.0;
        List<Invoice> invoices = invoiceRepo.
                findByCustomerAndStatusInOrderByCreatedAtAsc(
                        customer,
                        List.of(InvoiceStatus.PARTIAL, InvoiceStatus.UNPAID)
                );

        for (Invoice invoice : invoices) {
            totalDue += invoice.getNetTotal() - allocationRepo.sumAllocatedByInvoice(invoice);
        }

        return totalDue;
    }

    public Double calculateTotalDueSum() {
        List<Invoice> invoices = invoiceRepo.findByStatusIn(
                List.of(InvoiceStatus.UNPAID, InvoiceStatus.PARTIAL));

        double totalDue = 0.0;
        for (Invoice invoice : invoices) {
            totalDue += invoice.getNetTotal() - allocationRepo.sumAllocatedByInvoice(invoice);
        }
        return totalDue;
    }
}
