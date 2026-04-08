package com.Deye.UserService.event;

public class UserValidatedEvent {
    Integer orderId;
    Integer userId;
    Boolean isValid;

    public UserValidatedEvent() {
    }

    public UserValidatedEvent(Integer orderId, Integer userId, Boolean isValid) {
        this.orderId = orderId;
        this.userId = userId;
        this.isValid = isValid;
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
}
