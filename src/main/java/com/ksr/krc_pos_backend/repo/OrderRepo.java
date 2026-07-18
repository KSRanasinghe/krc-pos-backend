package com.ksr.krc_pos_backend.repo;

import com.ksr.krc_pos_backend.model.Order;
import com.ksr.krc_pos_backend.model.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepo extends JpaRepository<Order, Integer> {
    Optional<Order> findByUuid(UUID uuid);

    @Query("SELECT o FROM Order o WHERE " +
            "(:status IS NULL OR o.status = :status) AND " +
            "(:orderNo IS NULL OR o.orderNo = :orderNo) AND " +
            "(:phone IS NULL OR o.customer.phone = :phone)")
    List<Order> findByFilters(@Param("status") OrderStatus status,
                              @Param("orderNo") String orderNo,
                              @Param("phone") String phone);

    long countByStatus(OrderStatus status);
}
