package com.chrisnkl.ebr.common.exception;

import com.chrisnkl.ebr.book.exception.AuthorCreationFailureException;
import com.chrisnkl.ebr.book.exception.BookCreationFailureException;
import com.chrisnkl.ebr.book.exception.CategoryCreationFailureException;
import com.chrisnkl.ebr.common.api.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CategoryCreationFailureException.class)
    public ResponseEntity<ApiResponse<?>> handleCategoryCreationFailureException(CategoryCreationFailureException e) {
        log.error("Error occurred while creating category: {}", e.getMessage());
        return ResponseEntity.status(500).body(ApiResponse.internalError("Failed to create category: " + e.getMessage()));
    }

    @ExceptionHandler(AuthorCreationFailureException.class)
    public ResponseEntity<ApiResponse<?>> handleAuthorCreationFailureException(AuthorCreationFailureException e) {
        log.error("Error occurred while creating author: {}", e.getMessage());
        return ResponseEntity.status(500).body(ApiResponse.internalError("Failed to create author: " + e.getMessage()));
    }

    @ExceptionHandler(BookCreationFailureException.class)
    public ResponseEntity<ApiResponse<?>> handleBookCreationFailureException(BookCreationFailureException e) {
        log.error("Error occurred while creating book: {}", e.getMessage());
        return ResponseEntity.status(500).body(ApiResponse.internalError("Failed to create book: " + e.getMessage()));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleNoResourceFoundException(NoResourceFoundException e) {
        log.error("An unexpected error occurred.", e);
        return ResponseEntity.status(404).body(ApiResponse.notFound("Resource not found: " + e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGenericException(Exception e) {
        log.error("An unexpected error occurred.", e);
        return ResponseEntity.status(500).body(ApiResponse.internalError("An unexpected error occurred: " + e.getMessage()));
    }

}
