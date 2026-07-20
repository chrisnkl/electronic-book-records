package com.chrisnkl.ebr.batch.domain;

public record BookCsvRecord(

        String isbn,
        String title,
        String author,
        String category,
        String publisher,
        Short publicationYear,
        String description


) {
}
