package com.ksr.krc_pos_backend.service;

import com.ksr.krc_pos_backend.dto.*;
import com.ksr.krc_pos_backend.model.Customer;
import com.ksr.krc_pos_backend.model.Order;
import com.ksr.krc_pos_backend.model.OrderItem;
import com.ksr.krc_pos_backend.model.TaskVariant;
import com.ksr.krc_pos_backend.repo.CustomerRepo;
import com.ksr.krc_pos_backend.repo.OrderItemRepo;
import com.ksr.krc_pos_backend.repo.OrderRepo;
import com.ksr.krc_pos_backend.repo.TaskVariantsRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CustomerRepo customerRepo;
    private final CustomerService customerService;
    private final OrderRepo orderRepo;
    private final OrderItemRepo orderItemRepo;
    private final TaskVariantsRepo variantsRepo;

    @Transactional
    public OrderSummaryDto createOrder(OrderRequestDto orderReqDto) {
        //1. find customer, if not save a new one
        Customer customer = customerRepo.findByPhone(orderReqDto.getCustomer().getPhone())
                .orElseGet(() -> customerService.createCustomer(orderReqDto.getCustomer()));

        //2. save new order
        Order order = Order.builder().customer(customer).build();
        Order savedOrder = orderRepo.save(order);

        //3. iterating the item list and saving each item to db
        List<OrderItem> orderItems = orderReqDto.getOrderItems()
                .stream()
                .map(itemDto -> {
                    //get the matching variant
                    TaskVariant variant = variantsRepo.findByUuid(itemDto.getVariantUuid())
                            .orElseThrow(() -> new RuntimeException("Variant not found"));

                    return OrderItem.builder()
                            .quantity(itemDto.getQuantity())
                            .unitPrice(variant.getPrice())
                            .taskVariant(variant)
                            .order(savedOrder)
                            .build();
                })
                .toList();

        orderItemRepo.saveAll(orderItems);

        return OrderSummaryDto.builder()
                .uuid(savedOrder.getUuid())
                .status(savedOrder.getStatus())
                .customer(CustomerDto.builder()
                        .uuid(customer.getUuid())
                        .name(customer.getName())
                        .phone(customer.getPhone())
                        .note(customer.getNote())
                        .build())
                .createdAt(savedOrder.getCreatedAt())
                .build();
    }

    public List<OrderSummaryDto> getAllOrders() {
        return orderRepo.findAll()
                .stream()
                .map(order -> OrderSummaryDto.builder()
                        .uuid(order.getUuid())
                        .status(order.getStatus())
                        .customer(CustomerDto.builder()
                                .uuid(order.getCustomer().getUuid())
                                .name(order.getCustomer().getName())
                                .phone(order.getCustomer().getPhone())
                                .note(order.getCustomer().getNote())
                                .build())
                        .createdAt(order.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    public OrderDetailedDto getOrder(UUID uuid) {
        Order order = orderRepo.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        List<OrderItemResDto> orderItems = order.getOrderItems()
                .stream()
                .map(item -> OrderItemResDto.builder()
                        .uuid(item.getUuid())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
                        .taskName(item.getTaskVariant().getDesignTask().getName())
                        .variantLabel(item.getTaskVariant().getLabel())
                        .build())
                .toList();

        return OrderDetailedDto.builder()
                .order(OrderSummaryDto.builder()
                        .uuid(order.getUuid())
                        .status(order.getStatus())
                        .customer(CustomerDto.builder()
                                .uuid(order.getCustomer().getUuid())
                                .name(order.getCustomer().getName())
                                .phone(order.getCustomer().getPhone())
                                .note(order.getCustomer().getNote())
                                .build())
                        .createdAt(order.getCreatedAt())
                        .build())
                .orderItems(orderItems)
                .build();
    }
}
