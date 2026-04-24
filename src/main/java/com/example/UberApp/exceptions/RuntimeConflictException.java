package com.example.UberApp.exceptions;

public class RuntimeConflictException extends RuntimeException{

    public RuntimeConflictException(String message){
        super(message);
    }

    public RuntimeConflictException() {
    }
}
