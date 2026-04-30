package com.Deye.OrderService.controller;

import com.Deye.OrderService.dto.OrderRequest;
import com.Deye.OrderService.entity.Order;
import com.Deye.OrderService.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final Logger logger = LoggerFactory.getLogger(OrderController.class);

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public Order create(@RequestBody OrderRequest orderRequest) throws RuntimeException{
        String correlationId = UUID.randomUUID().toString();
        MDC.put("correlationId", correlationId);

        logger.info("Entering OrderController.create()");

        logger.info("CorrelationId: " + correlationId);

        if(orderRequest.getPrice().compareTo(BigDecimal.ZERO) <= 0) throw new RuntimeException("Price can't be lower than 0");
        if(orderRequest.getProductName().isEmpty()) throw new RuntimeException("Product name can't be empty");
        if(orderRequest.getQuantity() <= 0) throw new RuntimeException("Quantity can't be less than 1");

        logger.info("orderRequest received{} ", orderRequest);

        Order order = new Order();
        order.setUserId(orderRequest.getUserId());
        order.setPrice(orderRequest.getPrice());
        order.setQuantity(orderRequest.getQuantity());
        order.setProductName(orderRequest.getProductName());
        order.setEmail(orderRequest.getEmail());

        Order orderCreated = orderService.createOrder(order);

        logger.info("exiting OrderController.create()");
        MDC.clear();

        return orderCreated;
    }

    @GetMapping("/{id}")
    public Optional<Order> findById(@PathVariable int id) {
        String correlationId = UUID.randomUUID().toString();
        MDC.put("correlationId", correlationId);

        logger.info("Entering OrderController.findById()");
        logger.info("CorrelationId: " + correlationId);
        logger.info("Order id is {}", id);

        Optional<Order> order = orderService.getOrderById(id);

        logger.info("exiting OrderController.findById()");
        MDC.clear();
        return order;
    }
}
