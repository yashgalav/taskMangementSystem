package com.taskManagementSystem.exception;

import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException{

    private final HttpStatus httpStatus;
    private final String message;


    public CustomException( String message, HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
