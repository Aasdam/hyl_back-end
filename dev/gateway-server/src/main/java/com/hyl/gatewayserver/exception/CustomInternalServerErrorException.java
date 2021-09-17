package com.hyl.gatewayserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class CustomInternalServerErrorException extends RuntimeException {
    public CustomInternalServerErrorException() {}

    public CustomInternalServerErrorException(String message) {
        super(message);
    }

    public CustomInternalServerErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomInternalServerErrorException(Throwable cause) {
        super(cause);
    }

    public CustomInternalServerErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
