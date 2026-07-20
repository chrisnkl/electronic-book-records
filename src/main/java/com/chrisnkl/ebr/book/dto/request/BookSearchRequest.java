package com.chrisnkl.ebr.book.dto.request;

import com.chrisnkl.ebr.book.domain.BookSortField;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Sort;

public record BookSearchRequest(

        // Search fields
        String isbn,
        String title,
        String author,
        String category,
        String publisher,
        Short publicationYear,

        // Pagination
        @NotNull @Min(0) Integer page,
        @NotNull @Min(10) @Max(100) Integer size,

        // Sorting
        BookSortField sortBy,
        Sort.Direction sortDirection

) { }
