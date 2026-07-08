package com.deye.userService.exception;

public class UserAlreadyExist extends RuntimeException{
    public UserAlreadyExist(String email){
        super("user already exist " + email);
    }
}
