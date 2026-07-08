package com.deye.userService.unitTest;

import com.deye.userService.controller.UserServiceController;
import com.deye.userService.domain.dto.UserDto;
import com.deye.userService.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserServiceController.class)
class UserServiceControllerTest {

    @MockitoBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createUser() {
        UserDto user1 = new UserDto();
        user1.setUserName("user1");
        user1.setEmail("email1@gmail.com");
        user1.setUserPassword("123Abc@");

        when(userService.createUser(any())).thenReturn(user1);

        try {
            mockMvc.perform(post("/user/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                        {
                        "userName" : "user1",
                        "email" : "email1@gmail.com",
                        "userPassword" : "123Abc@"
                        }
                        """))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.userName").value(user1.getUserName()))
                    .andExpect(jsonPath("$.email").value(user1.getEmail()));
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}