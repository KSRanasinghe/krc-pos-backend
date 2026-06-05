package com.ksr.krc_pos_backend.service;

import com.ksr.krc_pos_backend.dto.CustomerDto;
import com.ksr.krc_pos_backend.dto.OrderDetailDto;
import com.ksr.krc_pos_backend.dto.OrderSummaryDto;
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
    public OrderSummaryDto createOrder(OrderDetailDto orderDetailDto) {
        //1. find customer, if not save a new one
        Customer customer = customerRepo.findByPhone(orderDetailDto.getCustomer().getPhone())
                .orElseGet(() -> customerService.createCustomer(orderDetailDto.getCustomer()));

        //2. save new order
        Order order = Order.builder().customer(customer).build();
        Order savedOrder = orderRepo.save(order);

        //3. iterating the item list and saving each item to db
        List<OrderItem> orderItems = orderDetailDto.getOrderItems()
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
}
