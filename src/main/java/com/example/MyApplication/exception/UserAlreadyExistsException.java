package com.example.MyApplication.exception;

import org.springframework.dao.DataIntegrityViolationException;

public class UserAlreadyExistsException extends DataIntegrityViolationException {

    public UserAlreadyExistsException(String message){
        super(message);
    }
}
