package com.chrisnkl.ebr.book.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum BookSortField {

    TITLE("title"),
    PUBLICATION_YEAR("publicationYear"),
    PUBLISHER("publisher"),
    ISBN("isbn");

    private final String property;

}
