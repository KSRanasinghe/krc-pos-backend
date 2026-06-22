package com.ksr.krc_pos_backend.repo;

import com.ksr.krc_pos_backend.model.Invoice;
import com.ksr.krc_pos_backend.model.enums.InvoiceStatus;
import com.ksr.krc_pos_backend.model.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InvoiceRepo extends JpaRepository<Invoice, Integer> {
    Optional<Invoice> findByUuid(UUID uuid);

    @Query("SELECT i FROM Invoice i WHERE " +
            "(:invNo IS NULL OR i.invNumber = :invNo) AND " +
            "(:status IS NULL OR i.status = :status) AND " +
            "(:phone IS NULL OR i.customer.phone = :phone)")
    List<Invoice> findByFilters(
            @Param("invNo") String invNo,
            @Param("phone") String phone,
            @Param("status") InvoiceStatus status);
}
