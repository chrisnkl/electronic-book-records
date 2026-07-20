package com.chrisnkl.ebr.common.api;

import org.springframework.http.HttpStatus;

import java.time.Instant;

public record ApiResponse<T>(

        int status,
        String message,
        Instant timestamp,
        T data

) {

    public static <T> ApiResponse<T> ok(String message) {
        return new ApiResponse<>(HttpStatus.OK.value(), message, Instant.now(), null);
    }

    public static <T> ApiResponse<T> ok(String message, T data) {
        return new ApiResponse<>(HttpStatus.OK.value(), message, Instant.now(), data);
    }

    public static <T> ApiResponse<T> internalError(String message) {
        return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), message, Instant.now(), null);
    }

    public static <T> ApiResponse<T> internalError(String message, T data) {
        return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), message, Instant.now(), data);
    }

    public static ApiResponse<?> notFound(String message) {
        return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), message, Instant.now(), null);
    }
}
