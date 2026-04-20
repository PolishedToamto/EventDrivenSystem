package com.Deye.OrderService.unitTest;

import com.Deye.OrderService.entity.Order;
import com.Deye.OrderService.event.OrderCreatedEvent;
import com.Deye.OrderService.producer.OrderEventProducer;
import com.Deye.OrderService.repository.OrderRepository;
import com.Deye.OrderService.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.hamcrest.Matchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderEventProducer orderEventProducer;

    @InjectMocks
    private OrderService orderService;

    @Test
    public void createOrder(){
        Order newOrder = new Order();
        newOrder.setOrderId(1);
        newOrder.setUserId(0);
        newOrder.setPrice(new BigDecimal(15.99));
        newOrder.setProductName("test");
        newOrder.setQuantity(1);
        newOrder.setEmail("test@gmail.com");

        when(orderRepository.save(argThat(order -> order.getOrderId() == 1 && order.getUserId() == 0)))
            .thenReturn(newOrder);

        orderService.createOrder(newOrder);

        verify(orderRepository).save(eq(newOrder));

        verify(orderEventProducer).sendOrderCreatedEvent(argThat(event -> event.getOrderId() == newOrder.getOrderId()
                && event.getUserId() == newOrder.getUserId()
                && event.getProductName() == newOrder.getProductName()
                && event.getQuantity() == newOrder.getQuantity()
                && event.getPrices().compareTo(event.getPrices()) == 0
                && event.getEmail() == newOrder.getEmail())
        );
    }

    @Test
    public void getOrderById(){
        orderRepository.findById(1);

        verify(orderRepository).findById(eq(1));
    }
}
