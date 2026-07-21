package com.chrisnkl.ebr.book.exception;

import com.chrisnkl.ebr.common.exception.BackendException;
import org.springframework.http.HttpStatus;

public class AuthorCreationFailureException extends BackendException {

    public AuthorCreationFailureException(String message, HttpStatus status) {
        super(message, status);
    }

    public AuthorCreationFailureException(String message, Throwable cause, HttpStatus status) {
        super(message, cause, status);
    }
}
