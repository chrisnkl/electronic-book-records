package com.chrisnkl.ebr.book.exception;

import com.chrisnkl.ebr.common.exception.BackendException;
import org.springframework.http.HttpStatus;


public class BookCreationFailureException extends BackendException {

    public BookCreationFailureException(String message, HttpStatus status) {
        super(message, status);
    }

    public BookCreationFailureException(String message, Throwable cause, HttpStatus status) {
        super(message, cause, status);
    }

}
