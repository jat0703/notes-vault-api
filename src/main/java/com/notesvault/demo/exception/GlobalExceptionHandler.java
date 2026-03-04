package com.notesvault.demo.exception;



import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(ResourceNotFoundException ex) {
        return buildErrorResponse("Not Found", ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getFieldErrors().getFirst().getDefaultMessage();
        return buildErrorResponse("Bad Request", msg, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Map<String, String>> buildErrorResponse(String error, String message, HttpStatus status) {
        Map<String, String> body = new HashMap<>();
        body.put("error", error);
        body.put("message", message);
        return new ResponseEntity<>(body, status);
    }
}