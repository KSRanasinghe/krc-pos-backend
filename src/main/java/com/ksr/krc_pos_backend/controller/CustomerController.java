package com.ksr.krc_pos_backend.controller;

import com.ksr.krc_pos_backend.dto.CustomerDto;
import com.ksr.krc_pos_backend.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    // Testing purposes only
//    @PostMapping("/")
//    public ResponseEntity<CustomerDto> createCustomer(@RequestBody CustomerDto customerDto) {
//        return ResponseEntity.ok(customerService.createCustomer(customerDto));
//    }

    @GetMapping("/search")
    public ResponseEntity<List<CustomerDto>> searchByPhone(@RequestParam String phone) {
        return ResponseEntity.ok(customerService.searchByPhone(phone));
    }
}
