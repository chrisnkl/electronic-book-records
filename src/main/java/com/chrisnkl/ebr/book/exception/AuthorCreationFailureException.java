package com.chrisnkl.ebr.book.exception;

import lombok.Getter;

@Getter
public class AuthorCreationFailureException extends RuntimeException {

    public AuthorCreationFailureException(String message) {
        super(message);
    }

    public AuthorCreationFailureException(String message, Throwable cause) {
        super(message, cause);
    }

}
