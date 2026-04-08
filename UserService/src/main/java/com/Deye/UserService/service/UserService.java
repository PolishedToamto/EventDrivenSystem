package com.Deye.UserService.service;

import org.springframework.stereotype.Service;

@Service
public class UserService {
    public boolean isValidUser(Integer userId){
        if(userId == null) return false;
        if(userId == 0) return true;

        return false;
    }


}
