package com.Deye.NotificationService.event;

import java.math.BigDecimal;

public class OrderCreatedEvent {
    private Integer orderId;
    private String productName;
    private Integer quantity;
    private BigDecimal prices;

    public OrderCreatedEvent(){

    }

    public OrderCreatedEvent(Integer orderId, String productName, Integer quantity, BigDecimal prices) {
        this.orderId = orderId;
        this.productName = productName;
        this.quantity = quantity;
        this.prices = prices;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
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
}
