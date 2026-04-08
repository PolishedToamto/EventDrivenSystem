package com.Deye.OrderService.service;

import com.Deye.OrderService.entity.Order;
import com.Deye.OrderService.event.OrderCreatedEvent;
import com.Deye.OrderService.producer.OrderEventProducer;
import com.Deye.OrderService.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderEventProducer orderEventProducer;

    public OrderService(OrderRepository orderRepository, OrderEventProducer orderEventProducer) {
        this.orderRepository = orderRepository;
        this.orderEventProducer = orderEventProducer;
    }

    public Order createOrder(Order order){
        Order savedOrder = orderRepository.save(order);

        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent(order.getOrderId(), order.getUserId(),order.getProductName(),order.getQuantity(),order.getPrice());

        orderEventProducer.sendOrderCreatedEvent(orderCreatedEvent);
        return savedOrder;
    }

    public Optional<Order> getOrderById(Integer orderId){
        return orderRepository.findById(orderId);
    }
}
