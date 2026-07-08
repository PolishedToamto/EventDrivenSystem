package com.deye.userService.service;

import com.deye.userService.domain.LoginRequest;
import com.deye.userService.domain.LoginResponse;
import com.deye.userService.domain.dto.UserDto;
import com.deye.userService.domain.entity.User;
import com.deye.userService.domain.mapper.UserMapper;
import com.deye.userService.exception.UserAlreadyExist;
import com.deye.userService.exception.WrongEmailOrPassword;
import com.deye.userService.repository.UserRepository;
import com.deye.userService.util.CommonUtil;
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
    private final static String CLASS_NAME = UserService.class.getSimpleName();
    private final static String ENTER_MESSAGE = "Entering " + CLASS_NAME + ".";
    private final static String EXIT_MESSAGE = "Exiting " + CLASS_NAME + ".";

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper, JWTService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.jwtService = jwtService;
    }

    public Optional<User> getUserNameById(int id){
        return userRepository.findById(id);
    }

    public UserDto createUser(UserDto userDto){
        String methodName = "createUser";
        logger.info(ENTER_MESSAGE + methodName);

        if(!CommonUtil.isValidUserName(userDto.getUserName())
                || !CommonUtil.isValidEmail(userDto.getEmail())
                || !CommonUtil.isValidPassword(userDto.getUserPassword())
        ) throw new RuntimeException("Invalid username or password");

        if(userRepository.existsByEmail(userDto.getEmail())) throw new UserAlreadyExist(userDto.getEmail());

        User user = userMapper.toEntity(userDto);
        user.setPasswordHash(passwordEncoder.encode(userDto.getUserPassword()));

        User savedUser = userRepository.save(user);
        Optional<User> refreshedUser = userRepository.findById(savedUser.getId());

        logger.info(EXIT_MESSAGE + methodName);
        return refreshedUser.isPresent() ? userMapper.toDto(refreshedUser.get()) : null;
    }

    public void removeUser(String email){
        String methodName = "removeUser";
        logger.info(ENTER_MESSAGE + methodName);

        User user = userRepository.findByEmail(email);
        if(user != null) {
            userRepository.deleteById(user.getId());
        }

        logger.info(EXIT_MESSAGE + methodName);
    }

    public LoginResponse login(LoginRequest request){
        String methodName = "login";
        logger.info(ENTER_MESSAGE + methodName);

        User user = userRepository.findByEmail(request.getEmail());

        if(user == null) throw new WrongEmailOrPassword(user.getEmail());

        boolean passwordEncodeMatch = passwordEncoder.matches(request.getPassword(), user.getPasswordHash());

        if(!passwordEncodeMatch) throw new WrongEmailOrPassword(user.getEmail());

        String token = jwtService.generateAccessToken(user);

        logger.info(EXIT_MESSAGE + methodName);
        return new LoginResponse(token);
    }
}
