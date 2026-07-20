package com.chrisnkl.ebr.book.exception;

import lombok.Getter;

@Getter
public class BookCreationFailureException extends RuntimeException {

    public BookCreationFailureException(String message) {
        super(message);
    }

    public BookCreationFailureException(String message, Throwable cause) {
        super(message, cause);
    }

}
