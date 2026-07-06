package com.deye.userService.util;

public class CommonUtil {

    public static boolean isValidEmail(String email){
        if(isEmptyOrNull(email)) return false;

        return email.matches("^[a-zA-Z1-9.+-]+@[a-zA-Z1-9]+.[a-zA-Z]{2,}$");
    }

    public static boolean isValidPassword(String password){
        boolean hasLowerCase = false;
        boolean hasUpperCase = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;

        for(char c : password.toCharArray()){
            if(Character.isLowerCase(c)) hasLowerCase = true;
            else if(Character.isUpperCase(c)) hasUpperCase = true;
            else if(Character.isDigit(c)) hasNumber = true;
            else hasSpecial = true;
        }

        return hasLowerCase && hasUpperCase && hasNumber && hasSpecial;
    }

    public static boolean isValidUserName(String userName){
        return !isEmptyOrNull(userName);
    }

    public static boolean isEmptyOrNull(String s){
        return s == null || s.isEmpty();
    }
}
