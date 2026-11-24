package com.ksr.krc_pos_backend.repo;

import com.ksr.krc_pos_backend.model.Invoice;
import com.ksr.krc_pos_backend.model.Payment;
import com.ksr.krc_pos_backend.model.enums.InvoiceStatus;
import com.ksr.krc_pos_backend.model.enums.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepo extends JpaRepository<Payment, Integer> {
    Optional<Payment> findByUuid(UUID uuid);

    List<Payment> findByCustomerPhoneContaining(String phone);

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE " +
            "p.method <> :excluded AND " +
            "MONTH(p.paidAt) = MONTH(CURRENT_DATE) AND " +
            "YEAR(p.paidAt) = YEAR(CURRENT_DATE)")
    Double getMonthlyRevenue(@Param("excluded") PaymentMethod excluded);
}
