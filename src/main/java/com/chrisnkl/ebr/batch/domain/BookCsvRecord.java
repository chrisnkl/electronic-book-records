package com.chrisnkl.ebr.batch.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookCsvRecord {

    private String isbn;
    private String title;
    private String author;
    private String category;
    private String publisher;
    private Short publicationYear;
    private String description;

}
