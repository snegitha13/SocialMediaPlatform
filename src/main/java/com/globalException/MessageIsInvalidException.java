package com.globalException;

public class MessageIsInvalidException extends RuntimeException {
    public MessageIsInvalidException(String message) {
        super(message);
    }
}