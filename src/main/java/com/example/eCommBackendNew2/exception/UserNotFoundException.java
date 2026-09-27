package com.example.eCommBackendNew2.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String message){
            super(message);
    }
}
