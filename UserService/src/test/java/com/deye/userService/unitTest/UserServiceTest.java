package com.deye.userService.unitTest;

import com.Deye.UserService.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Test
    public void testIsValidUserId(){
        Integer invalidUserId1 = -1;
        Integer invalidUserId2 = -123;

        Integer validUserId1 = 1;
        Integer validUserId2 = 2;

        Assertions.assertFalse(userService.isValidUser(invalidUserId1));
        Assertions.assertFalse(userService.isValidUser(invalidUserId2));

        Assertions.assertTrue(userService.isValidUser(validUserId1));
        Assertions.assertTrue(userService.isValidUser(validUserId2));
    }

    @Test
    public void testIsValidEmail() {
        String inValidEmail1 = "";
        String inValidEmail2 = "testEmail.com";

        String validEmail1 = "testEmail@com";
        String validEmail2 = "helloEmail@com";

        Assertions.assertFalse(userService.isValidEmail(inValidEmail1));
        Assertions.assertFalse(userService.isValidEmail(inValidEmail2));

        Assertions.assertTrue(userService.isValidEmail(validEmail1));
        Assertions.assertTrue(userService.isValidEmail(validEmail2));
    }
}
