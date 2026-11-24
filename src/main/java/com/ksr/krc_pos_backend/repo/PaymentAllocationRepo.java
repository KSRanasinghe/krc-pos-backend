package com.ksr.krc_pos_backend.repo;

import com.ksr.krc_pos_backend.model.Invoice;
import com.ksr.krc_pos_backend.model.Payment;
import com.ksr.krc_pos_backend.model.PaymentAllocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaymentAllocationRepo extends JpaRepository<PaymentAllocation, Integer> {

    @Query("SELECT COALESCE(SUM(a.allocatedAmount), 0) FROM PaymentAllocation a WHERE a.invoice = :invoice")
    Double sumAllocatedByInvoice(@Param("invoice") Invoice invoice);

    List<PaymentAllocation> findByPayment(Payment payment);
}
