package com.chrisnkl.ebr.book.service;

import com.chrisnkl.ebr.book.domain.BookSortField;
import com.chrisnkl.ebr.book.dto.request.BookRequest;
import com.chrisnkl.ebr.book.dto.request.BookSearchRequest;
import com.chrisnkl.ebr.book.dto.response.BookImportResponse;
import com.chrisnkl.ebr.book.dto.response.BookResponse;
import com.chrisnkl.ebr.book.entity.Author;
import com.chrisnkl.ebr.book.entity.Book;
import com.chrisnkl.ebr.book.entity.Category;
import com.chrisnkl.ebr.book.exception.BookCreationFailureException;
import com.chrisnkl.ebr.book.mapper.BookMapper;
import com.chrisnkl.ebr.book.repository.BookRepository;
import com.chrisnkl.ebr.book.specification.BookSpecification;
import com.chrisnkl.ebr.common.exception.BackendException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorService authorService;
    private final CategoryService categoryService;

    private final JobLauncher jobLauncher;

    @Qualifier("importBooksJob")
    private final Job job;

    private final BookMapper bookMapper;


    public Page<BookResponse> getBooks(BookSearchRequest request) {

        int page = Optional.ofNullable(request.page()).orElse(0);
        int size = Optional.ofNullable(request.size()).orElse(20);

        BookSortField sortBy = Optional.ofNullable(request.sortBy()).orElse(BookSortField.TITLE);
        Sort.Direction sortDirection = Optional.ofNullable(request.sortDirection()).orElse(Sort.Direction.ASC);

        Specification<Book> specification = BookSpecification.withFilters(request);

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy.getProperty()));
        log.info("BookService.getBooks is called with pageable: {}", pageable);

        return bookRepository.findAll(specification, pageable)
                .map(bookMapper::toResponse);
    }

    @Transactional
    public BookResponse create(BookRequest request) {
        log.info("BookService.create is called with request: {}", request);

        try {

            Author author = authorService.findOrCreate(request.author());
            Category category = categoryService.findOrCreate(request.category());

            Book book = Book.builder()
                    .isbn(request.isbn())
                    .title(request.title())
                    .author(author)
                    .category(category)
                    .publisher(request.publisher())
                    .publicationYear(request.publicationYear())
                    .description(request.description())
                    .build();

            Book savedBook = bookRepository.save(book);

            log.info("Successfully created book: {}", savedBook.getId());
            return bookMapper.toResponse(savedBook);

        } catch (Exception e) {
            log.error("Error occurred while creating book: {} by {}", request.title(), request.author());
            throw new BookCreationFailureException("Failed to create book " + request.title() + " by " + request.author(), e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public BookImportResponse importBooks(MultipartFile file) {

        log.info("BookService.importBooks is initiating a request..");

        if (file == null || file.isEmpty()) throw new BackendException("File is not specified or empty.", HttpStatus.BAD_REQUEST);
        Path tempFile = null;

        try {

            tempFile = Files.createTempFile("books-", ".csv");
            file.transferTo(tempFile);

            JobParameters params = new JobParametersBuilder()
                    .addString("filePath", tempFile.toAbsolutePath().toString())
                    .addLong("timestamp", Instant.now().toEpochMilli())
                    .toJobParameters();

            jobLauncher.run(job, params);
            return new BookImportResponse(0, 0, 0, 0, List.of());


        } catch (Exception e) {
            log.error("Failed to import books.", e);
            throw new BackendException("Failed to import the books from CSV.", e, HttpStatus.INTERNAL_SERVER_ERROR);

        } finally {
            if (tempFile != null) {
                try {
                    Files.deleteIfExists(tempFile);
                } catch (IOException e) {
                    log.warn("Failed to delete temporary file={}", tempFile);
                }
            }
        }
    }
}
