package com.ksr.krc_pos_backend.service;

import com.ksr.krc_pos_backend.dto.CustomerDto;
import com.ksr.krc_pos_backend.dto.CustomerDueDto;
import com.ksr.krc_pos_backend.model.Customer;
import com.ksr.krc_pos_backend.repo.CustomerRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepo customerRepo;
    private final AllocationService allocationService;

    public List<CustomerDto> searchByPhone(String phone) {
        return customerRepo.findByPhoneContaining(phone)
                .stream()
                .map(customer -> CustomerDto.builder()
                        .uuid(customer.getUuid())
                        .name(customer.getName())
                        .phone(customer.getPhone())
                        .note(customer.getNote())
                        .walletBalance(customer.getWalletBalance())
                        .build())
                .collect(Collectors.toList());
    }

    public Customer createCustomer(CustomerDto customerDto) {
        Customer customer = Customer.builder()
                .name(customerDto.getName())
                .phone(customerDto.getPhone())
                .note(customerDto.getNote())
                .build();

        return customerRepo.save(customer);
    }

    public CustomerDueDto getCustomerDue(String phone) {
        // 1. Find the customer by phone (findByPhone)
        //    If not found, throw RuntimeException
        Customer customer = customerRepo.findByPhone(phone)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // 2. Call allocationService.calculateTotalDue(customer)
        Double totalDue = allocationService.calculateTotalDue(customer);

        // 3. Build and return CustomerDueDto
        return CustomerDueDto.builder()
                .totalDue(totalDue).build();
    }

    public List<CustomerDto> getAllCustomers(String phone) {
        return customerRepo.findByPhoneContaining(phone != null ? phone : "")
                .stream()
                .map(customer -> CustomerDto.builder()
                        .uuid(customer.getUuid())
                        .name(customer.getName())
                        .phone(customer.getPhone())
                        .note(customer.getNote())
                        .walletBalance(customer.getWalletBalance())
                        .build())
                .collect(Collectors.toList());
    }
}
