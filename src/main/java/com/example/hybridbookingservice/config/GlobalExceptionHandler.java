package com.example.hybridbookingservice.config;

import com.example.hybridbookingservice.exceptions.DataNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {DataNotFoundException.class})
    public ResponseEntity<String> dataNotFound(DataNotFoundException e) {
        return ResponseEntity.status(404).body(e.getMessage());
    }
    @ExceptionHandler(value = {AccessDeniedException.class})
    public ResponseEntity<String> accessDenied(AccessDeniedException e) {
        return ResponseEntity.status(403).body(e.getMessage());
    }

}
