package com.ksr.krc_pos_backend.controller;

import com.ksr.krc_pos_backend.dto.OrderDetailDto;
import com.ksr.krc_pos_backend.dto.OrderSummaryDto;
import com.ksr.krc_pos_backend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/")
    public ResponseEntity<List<OrderSummaryDto>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @PostMapping("/")
    public ResponseEntity<OrderSummaryDto> createOrder(@RequestBody OrderDetailDto orderDetailDto) {
        return new ResponseEntity<>(orderService.createOrder(orderDetailDto), HttpStatus.CREATED);
    }
}
