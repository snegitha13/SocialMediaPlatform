package com.globalException;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NoDataFoundException extends RuntimeException {
    //private static final long serialVersionUID = 1L;

    public NoDataFoundException(String message) {
        super(message);
    }
}
