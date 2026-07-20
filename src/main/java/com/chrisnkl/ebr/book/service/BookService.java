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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorService authorService;
    private final CategoryService categoryService;
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
            throw new BookCreationFailureException("Failed to create book " + request.title() + " by " + request.author(), e);
        }
    }

    public BookImportResponse importBooks(MultipartFile file) {
        
        return null;

    }
}
