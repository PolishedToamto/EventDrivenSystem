package com.deye.userService.exceptionHandler;

import com.deye.userService.exception.UserAlreadyExist;
import com.deye.userService.exception.WrongEmailOrPassword;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.deye.userService.domain.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExist.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExist(UserAlreadyExist exception, HttpRequest request){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ErrorResponse(LocalDateTime.now(),
                        HttpStatus.CONFLICT.value(),
                        HttpStatus.CONFLICT.getReasonPhrase(),
                        exception.getMessage(),
                        request.getURI().getPath())
        );
    }

    @ExceptionHandler(WrongEmailOrPassword.class)
    public ResponseEntity<ErrorResponse> handleWrongEmailOrPassword(WrongEmailOrPassword exception, HttpRequest request){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new ErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.UNAUTHORIZED.value(),
                        HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                        exception.getMessage(),
                        request.getURI().getPath()
                )
        );
    }
}
