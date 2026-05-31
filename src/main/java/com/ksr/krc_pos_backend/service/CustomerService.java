package com.ksr.krc_pos_backend.service;

import com.ksr.krc_pos_backend.dto.CustomerDto;
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

    public List<CustomerDto> searchByPhone(String phone) {
        return customerRepo.findByPhoneContaining(phone)
                .stream()
                .map(customer -> CustomerDto.builder()
                        .uuid(customer.getUuid())
                        .name(customer.getName())
                        .phone(customer.getPhone())
                        .note(customer.getNote())
                        .isRegular(customer.isRegular())
                        .build())
                .collect(Collectors.toList());
    }

    public CustomerDto createCustomer(CustomerDto customerDto) {
        Customer customer = Customer.builder()
                .name(customerDto.getName())
                .phone(customerDto.getPhone())
                .note(customerDto.getNote())
                .isRegular(customerDto.getIsRegular())
                .build();

        Customer savedCustomer = customerRepo.save(customer);

        return CustomerDto.builder()
                .uuid(savedCustomer.getUuid())
                .name(savedCustomer.getName())
                .phone(savedCustomer.getPhone())
                .note(savedCustomer.getNote())
                .isRegular(savedCustomer.isRegular())
                .build();
    }
}
