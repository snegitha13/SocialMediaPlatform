package com.globalException;

public class LikesNotFoundException extends RuntimeException {
    public LikesNotFoundException(String message) {
        super(message);
    }
}
