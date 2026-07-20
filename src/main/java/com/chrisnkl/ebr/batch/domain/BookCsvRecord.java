package com.chrisnkl.ebr.batch.domain;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@ToString
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
