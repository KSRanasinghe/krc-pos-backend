package com.ksr.krc_pos_backend.repo;

import com.ksr.krc_pos_backend.model.Invoice;
import com.ksr.krc_pos_backend.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepo extends JpaRepository<Payment, Integer> {
    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.invoice = :invoice")
    Double sumByInvoice(@Param("invoice") Invoice invoice);
}
