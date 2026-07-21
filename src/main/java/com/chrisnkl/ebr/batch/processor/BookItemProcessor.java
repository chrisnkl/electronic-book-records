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

        Author author = authorService.findOrCreate(item.author());
        Category category = categoryService.findOrCreate(item.category());

        return Book.builder()
                .isbn(item.isbn().trim())
                .title(item.title().trim())
                .author(author)
                .category(category)
                .publisher(item.publisher().trim())
                .publicationYear(item.publicationYear())
                .description(item.description())
                .build();
    }
}
