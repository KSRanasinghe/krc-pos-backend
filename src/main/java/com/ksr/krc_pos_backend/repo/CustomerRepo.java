package com.ksr.krc_pos_backend.repo;

import com.ksr.krc_pos_backend.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Integer> {
    List<Customer> findByPhoneContaining(String phone);
}
