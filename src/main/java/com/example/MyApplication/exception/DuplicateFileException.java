package com.example.MyApplication.exception;

import software.amazon.awssdk.services.s3.endpoints.internal.Value;

public class DuplicateFileException extends RuntimeException{

    public DuplicateFileException(String message){
        super(message);
    }
}
