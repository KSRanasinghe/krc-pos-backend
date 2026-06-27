package com.ksr.krc_pos_backend.service;

import com.ksr.krc_pos_backend.dto.DashboardDto;
import com.ksr.krc_pos_backend.model.enums.InvoiceStatus;
import com.ksr.krc_pos_backend.model.enums.OrderStatus;
import com.ksr.krc_pos_backend.repo.InvoiceRepo;
import com.ksr.krc_pos_backend.repo.OrderRepo;
import com.ksr.krc_pos_backend.repo.PaymentRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final OrderRepo orderRepo;
    private final InvoiceRepo invoiceRepo;
    private final PaymentRepo paymentRepo;

    public DashboardDto getDashboard() {
        return DashboardDto.builder()
                .monthlyRevenue(paymentRepo.getMonthlyRevenue())
                .pendingOrders(orderRepo.countByStatus(OrderStatus.PENDING))
                .unpaidInvoices(invoiceRepo.countByStatus(InvoiceStatus.UNPAID))
                .build();
    }
}
