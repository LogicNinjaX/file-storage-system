package com.example.MyApplication.handler;

import com.example.MyApplication.dto.ApiResponse;
import com.example.MyApplication.dto.ErrorResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateKeyException(DataIntegrityViolationException ex) {

        ErrorResponse errorResponse = new ErrorResponse(
                false,
                ex.getLocalizedMessage(),
                HttpStatus.NOT_ACCEPTABLE.value(),
                null);

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_ACCEPTABLE);
    }
}
