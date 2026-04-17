package com.Deye.OrderService.unitTest;

import com.Deye.OrderService.controller.OrderController;
import com.Deye.OrderService.entity.Order;
import com.Deye.OrderService.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @Test
    public void getOrderById() throws Exception {
        when(orderService.getOrderById(1)).thenReturn(Optional.of(new Order(1, 0, "", 0, BigDecimal.ZERO, "")));
        mockMvc.perform(get("/orders/{id}", 1)).andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(1));

    }

    @Test
    public void createOrder() throws Exception {
        Order testOrder = new Order(1, 0, "testProduct", 1, new BigDecimal(15.99), "test@gmail.com");
        //when -> thenReturn only return the specified object when mockito see what was invoke
        when(orderService.createOrder(argThat(order -> order.getUserId() == 0 && order.getEmail().equalsIgnoreCase("test@gmail.com")))).thenReturn(testOrder);

        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        """
                               
                                {
                                   "userId" : 0,
                                  "productName": "testProduct",
                                  "quantity": 1,
                                  "price": 15.99,
                                  "email":"test@gmail.com"
                                }
                               
                                """
                ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(1));
    }
}
