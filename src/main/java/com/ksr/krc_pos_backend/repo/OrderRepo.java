package com.ksr.krc_pos_backend.repo;

import com.ksr.krc_pos_backend.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepo extends JpaRepository<Order, Integer> {
    Optional<Order> findByUuid(UUID uuid);
}
