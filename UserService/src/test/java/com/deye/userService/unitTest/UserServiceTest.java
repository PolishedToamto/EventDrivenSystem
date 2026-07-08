package com.deye.userService.unitTest;

import com.deye.userService.domain.dto.UserDto;
import com.deye.userService.domain.entity.User;
import com.deye.userService.domain.mapper.UserMapper;
import com.deye.userService.repository.UserRepository;
import com.deye.userService.service.UserService;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    UserRepository userRepository;

    @Mock
    UserMapper userMapper;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void createUserTest() {
        UserDto userDto1 = new UserDto();
        userDto1.setUserName("user1");
        userDto1.setUserPassword("123Abc@");
        userDto1.setEmail("correctEmail1@gmail.com");

        UserDto userDto2 = new UserDto();
        userDto2.setUserName("user2");
        userDto2.setUserPassword("123Abc");
        userDto2.setEmail("correctEmail2@gmail.com");

        UserDto userDto3 = new UserDto();
        userDto3.setUserName("user3");
        userDto3.setUserPassword("123Abc@");
        userDto3.setEmail("wronggmail.com");

        User user1 = new User();
        user1.setId(1);
        user1.setUserName(userDto1.getUserName());
        user1.setEmail(userDto1.getEmail());

        when(userMapper.toEntity(userDto1)).thenReturn(user1);
        when(userRepository.save(any(User.class))).thenReturn(user1);

        userService.createUser(userDto1);

        verify(userRepository).existsByEmail(userDto1.getEmail());

        verify(passwordEncoder).encode(userDto1.getUserPassword());

        verify(userRepository).save(any());
        verify(userRepository).findById(any());

        Assert.assertThrows(RuntimeException.class, () -> userService.createUser(userDto2));
        Assert.assertThrows(RuntimeException.class, () -> userService.createUser(userDto3));
    }

}
