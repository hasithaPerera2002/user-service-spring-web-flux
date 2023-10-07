package com.ijse.gdse.cw.hasitha.userService.advice;

import com.ijse.gdse.cw.hasitha.userService.exceptions.NotValidObjectException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

@ExceptionHandler(NotValidObjectException.class)
    public ResponseEntity<?> handleException(NotValidObjectException e){
        return ResponseEntity.badRequest().body(e.getViolations());
    }
}
