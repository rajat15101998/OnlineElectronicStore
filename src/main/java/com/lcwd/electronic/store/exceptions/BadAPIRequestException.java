package com.lcwd.electronic.store.exceptions;

public class BadAPIRequestException extends RuntimeException {
    public BadAPIRequestException() {
           super("Bad API Request");
    }
    public BadAPIRequestException(String message) {
        super(message);
    }
}
