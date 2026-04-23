package com.Deye.OrderService.event;

import java.math.BigDecimal;

public class OrderCreatedEvent {
    private Integer orderId;
    private Integer userId;
    private String productName;
    private Integer quantity;
    private BigDecimal prices;
    private String email;

    public OrderCreatedEvent(){}

    public OrderCreatedEvent(Integer orderId, Integer userId, String productName, Integer quantity, BigDecimal prices, String email) {
        this.orderId = orderId;
        this.userId = userId;
        this.productName = productName;
        this.quantity = quantity;
        this.prices = prices;
        this.email = email;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserid(Integer userId) {
        this.userId = userId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrices() {
        return prices;
    }

    public void setPrices(BigDecimal prices) {
        this.prices = prices;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
