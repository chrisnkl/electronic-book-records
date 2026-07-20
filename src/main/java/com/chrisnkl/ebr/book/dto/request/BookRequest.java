package com.chrisnkl.ebr.book.dto.request;

import jakarta.validation.constraints.NotBlank;

public record BookRequest(

        @NotBlank
        String isbn,

        @NotBlank
        String title,

        @NotBlank
        String author,

        @NotBlank
        String category,

        @NotBlank
        String publisher,

        Short publicationYear,

        String description

) {}