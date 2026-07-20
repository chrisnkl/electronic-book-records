package com.chrisnkl.ebr.book.dto.response;

import java.util.UUID;

public record BookResponse(
        UUID id,
        String isbn,
        String title,
        String author,
        String category,
        String publisher,
        Short publicationYear,
        String description
) {
}
