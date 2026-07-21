package com.chrisnkl.ebr.book.exception;

import com.chrisnkl.ebr.common.exception.BackendException;


public class CategoryCreationFailureException extends BackendException {

    public CategoryCreationFailureException(String message) {
        super(message);
    }

    public CategoryCreationFailureException(String message, Throwable cause) {
        super(message, cause);
    }

}
