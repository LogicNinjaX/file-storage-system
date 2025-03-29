package com.example.MyApplication.dto;

public class FolderCreationFailedException extends RuntimeException{

    public FolderCreationFailedException(String message){
        super(message);
    }
}
