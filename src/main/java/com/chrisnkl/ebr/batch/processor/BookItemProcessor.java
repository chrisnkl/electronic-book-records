package com.chrisnkl.ebr.batch.processor;

import com.chrisnkl.ebr.batch.domain.BookCsvRecord;
import com.chrisnkl.ebr.book.entity.Author;
import com.chrisnkl.ebr.book.entity.Book;
import com.chrisnkl.ebr.book.entity.Category;
import com.chrisnkl.ebr.book.service.AuthorService;
import com.chrisnkl.ebr.book.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
@Slf4j
@RequiredArgsConstructor
public class BookItemProcessor implements ItemProcessor<BookCsvRecord, Book> {

    private final AuthorService authorService;
    private final CategoryService categoryService;

    @Nullable
    @Override
    public Book process(@NonNull BookCsvRecord item) throws Exception {

        log.debug("Processing record: {}", item);

        if (isEmptyRow(item)) {
            log.warn("Skipping empty CSV row");
            return null;
        }

        if (!validate(item)) {
            log.warn("Failed validate a book csv record, skipping..");
            return null;
        }

        Author author = authorService.findOrCreate(item.getAuthor());
        Category category = categoryService.findOrCreate(item.getCategory());

        return Book.builder()
                .isbn(item.getIsbn().trim())
                .title(item.getTitle().trim())
                .author(author)
                .category(category)
                .publisher(item.getPublisher().trim())
                .publicationYear(item.getPublicationYear())
                .description(item.getDescription())
                .build();
    }

    private boolean isEmptyRow(BookCsvRecord item) {

        return Stream.of(
                item.getIsbn(),
                item.getTitle(),
                item.getAuthor(),
                item.getCategory(),
                item.getPublisher(),
                item.getDescription()
        ).allMatch(value -> value == null || value.isBlank());
    }

    private boolean validate(BookCsvRecord item) {

        if (item.getTitle() == null || item.getTitle().isBlank()) {
            throw new IllegalArgumentException("Book title is missing");
        }

        if (item.getAuthor() == null || item.getAuthor().isBlank()) {
            throw new IllegalArgumentException("Author is missing");
        }

        if (item.getPublicationYear() == null) {
            throw new IllegalArgumentException("Publication year is missing");
        }
        return true;
    }

}
