package com.Deye.OrderService.service;

import com.Deye.OrderService.entity.Order;
import com.Deye.OrderService.event.OrderCreatedEvent;
import com.Deye.OrderService.producer.OrderEventProducer;
import com.Deye.OrderService.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderEventProducer orderEventProducer;
    private final Logger logger = LoggerFactory.getLogger(OrderService.class);

    public OrderService(OrderRepository orderRepository, OrderEventProducer orderEventProducer) {
        this.orderRepository = orderRepository;
        this.orderEventProducer = orderEventProducer;
    }

    public Order createOrder(Order order){
        logger.info("Entering createOrder()");

        Order savedOrder = orderRepository.save(order);

        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent(order.getOrderId(), order.getUserId(),order.getProductName(),order.getQuantity(),order.getPrice(), order.getEmail());

        orderEventProducer.sendOrderCreatedEvent(orderCreatedEvent);
        logger.info("exiting createOrder()");
        return savedOrder;
    }

    public Optional<Order> getOrderById(Integer orderId){
        logger.info("Entering getOrderById()");

        Optional<Order> order = orderRepository.findById(orderId);

        logger.info("Exiting getOrderById()");
        return order;
    }
}
