package com.Deye.OrderService.controller;

import com.Deye.OrderService.dto.OrderRequest;
import com.Deye.OrderService.entity.Order;
import com.Deye.OrderService.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public Order create(@RequestBody OrderRequest orderRequest) throws RuntimeException{
        if(orderRequest.getPrice().compareTo(BigDecimal.ZERO) <= 0) throw new RuntimeException("Price can't be lower than 0");
        if(orderRequest.getProductName().isEmpty()) throw new RuntimeException("Product name can't be empty");
        if(orderRequest.getQuantity() <= 0) throw new RuntimeException("Quantity can't be less than 1");

        Order order = new Order();
        order.setPrice(orderRequest.getPrice());
        order.setQuantity(orderRequest.getQuantity());
        order.setProductName(orderRequest.getProductName());

        return orderService.createOrder(order);
    }

    @GetMapping("/{id}")
    public Optional<Order> findById(@PathVariable int id) {
        return orderService.getOrderById(id);
    }
}
