package com.deye.userService.event;

public class UserValidatedEvent {
    Integer orderId;
    Integer userId;
    String email;
    Boolean isValid;

    public UserValidatedEvent() {
    }

    public UserValidatedEvent(Integer orderId, Integer userId, String email,Boolean isValid) {
        this.orderId = orderId;
        this.userId = userId;
        this.isValid = isValid;
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

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Boolean getValid() {
        return isValid;
    }

    public void setValid(Boolean valid) {
        isValid = valid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
