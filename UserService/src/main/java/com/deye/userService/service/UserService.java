package com.deye.userService.service;

import com.deye.userService.domain.LoginRequest;
import com.deye.userService.domain.LoginResponse;
import com.deye.userService.domain.dto.UserDto;
import com.deye.userService.domain.entity.User;
import com.deye.userService.domain.mapper.UserMapper;
import com.deye.userService.repository.UserRepository;
import com.deye.userService.util.CommonUtil;
import org.apache.kafka.common.security.auth.Login;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JWTService jwtService;

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper, JWTService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.jwtService = jwtService;
    }

    public Optional<User> getUserNameById(int id){
        return userRepository.findById(id);
    }

    public boolean isValidUser(Integer userId){
        if(userId == null) return false;
        if(userId >= 0) return true;

        return false;
    }

    public boolean isValidEmail(String email) {
        if(email == null
                || email.equalsIgnoreCase("")
                || !email.contains("@")){
            return false;
        }
        return true;
    }

    public UserDto createUser(UserDto userDto){
        if(!CommonUtil.isValidUserName(userDto.getUserName())
                || !CommonUtil.isValidEmail(userDto.getEmail())
                || !CommonUtil.isValidPassword(userDto.getUserPassword())
        ) throw new RuntimeException("Invalid username or password");

        if(userRepository.existsByEmail(userDto.getEmail())) throw new RuntimeException("Email already registered");

        User user = userMapper.toEntity(userDto);
        user.setPasswordHash(passwordEncoder.encode(userDto.getUserPassword()));

        User savedUser = userRepository.save(user);
        Optional<User> refreshedUser = userRepository.findById(savedUser.getId());

        return refreshedUser.isPresent() ? userMapper.toDto(refreshedUser.get()) : null;
    }

    public LoginResponse login(LoginRequest request){
        User user = userRepository.findByEmail(request.getEmail());

        if(user == null) throw new RuntimeException("Invalid email/password");

        boolean passwordEncodeMatch = passwordEncoder.matches(request.getPassword(), user.getPasswordHash());

        if(!passwordEncodeMatch) throw new RuntimeException("Invalid email/password");

        String token = jwtService.generateAccessToken(user);
        return new LoginResponse(token, "JWT");
    }
}
