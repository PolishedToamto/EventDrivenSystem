package com.deye.userService.exception;

public class WrongEmailOrPassword extends RuntimeException{
    public WrongEmailOrPassword(String email){
        super("Incorrect email/password " + email);
    }
}
