package com.chrisnkl.ebr.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BackendException extends RuntimeException {

    private final HttpStatus status;

    public BackendException(String message, HttpStatus status) {
        super(message); this.status = status;
    }

    public BackendException(String message, Throwable cause, HttpStatus status) {
        super(message, cause);
        this.status = status;
    }
}
