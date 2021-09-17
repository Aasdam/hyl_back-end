package com.hyl.gatewayserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CustomNotFoundException extends RuntimeException {
    public CustomNotFoundException() {}

    public CustomNotFoundException(String message) {
        super(message);
    }

    public CustomNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomNotFoundException(Throwable cause) {
        super(cause);
    }

    public CustomNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
