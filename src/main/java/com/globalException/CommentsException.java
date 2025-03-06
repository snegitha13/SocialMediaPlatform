package com.globalException;

public class CommentsException extends RuntimeException {
    private String errorCode;

    public CommentsException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}