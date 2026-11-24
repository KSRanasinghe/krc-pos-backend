package com.ksr.krc_pos_backend.repo;

import com.ksr.krc_pos_backend.model.Customer;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Integer> {
    List<Customer> findByPhoneContaining(String phone);

    Optional<Customer> findByPhone(String phone);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Customer c WHERE c.phone = :phone")
    Optional<Customer> findByPhoneForUpdate(@Param("phone") String phone);
}
