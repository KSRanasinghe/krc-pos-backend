package com.ksr.krc_pos_backend.controller;

import com.ksr.krc_pos_backend.dto.OrderDetailedDto;
import com.ksr.krc_pos_backend.dto.OrderItemReqDto;
import com.ksr.krc_pos_backend.dto.OrderRequestDto;
import com.ksr.krc_pos_backend.dto.OrderSummaryDto;
import com.ksr.krc_pos_backend.model.enums.OrderStatus;
import com.ksr.krc_pos_backend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/")
    public ResponseEntity<List<OrderSummaryDto>> getAllOrders(
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) String phone) {
        return ResponseEntity.ok(orderService.getAllOrders(status, phone));
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<OrderDetailedDto> getOrder(@PathVariable UUID uuid) {
        return ResponseEntity.ok(orderService.getOrder(uuid));
    }

    @PostMapping("/")
    public ResponseEntity<OrderSummaryDto> createOrder(@RequestBody OrderRequestDto orderReqDto) {
        return new ResponseEntity<>(orderService.createOrder(orderReqDto), HttpStatus.CREATED);
    }

    @PostMapping("/{uuid}/items")
    public ResponseEntity<OrderSummaryDto> addNewItems(@PathVariable UUID uuid, @RequestBody List<OrderItemReqDto> items){
        return ResponseEntity.ok(orderService.addNewItems(uuid, items));
    }

    @PatchMapping("/{uuid}/status")
    public ResponseEntity<OrderSummaryDto> updateOrderStatus(
            @PathVariable UUID uuid, @RequestParam OrderStatus status) {
        return ResponseEntity.ok(orderService.updateOrderStatus(uuid, status));
    }

    @DeleteMapping("/items/{uuid}")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable UUID uuid) {
        orderService.deleteOrderItem(uuid);
        return ResponseEntity.noContent().build();
    }
}
