package com.chrisnkl.ebr.book.exception;

import lombok.Getter;

@Getter
public class CategoryCreationFailureException extends RuntimeException {

    public CategoryCreationFailureException(String message) {
        super(message);
    }

    public CategoryCreationFailureException(String message, Throwable cause) {
        super(message, cause);
    }

}
