package com.ksr.krc_pos_backend.controller;

import com.ksr.krc_pos_backend.dto.CustomerDto;
import com.ksr.krc_pos_backend.dto.CustomerDueDto;
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

    @GetMapping
    public ResponseEntity<List<CustomerDto>> getAllCustomers(
            @RequestParam(required = false) String phone
    ) {
        return ResponseEntity.ok(customerService.getAllCustomers(phone));
    }

    @GetMapping("/search")
    public ResponseEntity<List<CustomerDto>> searchByPhone(@RequestParam String phone) {
        return ResponseEntity.ok(customerService.searchByPhone(phone));
    }

    @GetMapping("/{phone}/due")
    public ResponseEntity<CustomerDueDto> getCustomerDue(@PathVariable String phone) {
        return ResponseEntity.ok(customerService.getCustomerDue(phone));
    }
}
