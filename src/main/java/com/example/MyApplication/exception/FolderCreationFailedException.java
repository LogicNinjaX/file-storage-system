package com.example.MyApplication.exception;

public class FolderCreationFailedException extends RuntimeException{

    public FolderCreationFailedException(String message){
        super(message);
    }
}
