package com.chrisnkl.ebr.book.exception;

import com.chrisnkl.ebr.common.exception.BackendException;
import org.springframework.http.HttpStatus;


public class CategoryCreationFailureException extends BackendException {

    public CategoryCreationFailureException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }

    public CategoryCreationFailureException(String message, Throwable cause, HttpStatus httpStatus) {
        super(message, cause, httpStatus);
    }

}
