package com.hyl.userapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class CustomUnauthorizedException extends RuntimeException{
    public CustomUnauthorizedException() {}

    public CustomUnauthorizedException(String message) {
        super(message);
    }

    public CustomUnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomUnauthorizedException(Throwable cause) {
        super(cause);
    }

    public CustomUnauthorizedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
