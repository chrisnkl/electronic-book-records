package com.chrisnkl.ebr.book.specification;

import com.chrisnkl.ebr.book.dto.request.BookSearchRequest;
import com.chrisnkl.ebr.book.entity.Author;
import com.chrisnkl.ebr.book.entity.Book;
import com.chrisnkl.ebr.book.entity.Category;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecification {

    private BookSpecification() {}

    public static Specification<Book> withFilters(BookSearchRequest request) {
        return isbnContains(request.isbn())
                .and(titleContains(request.title()))
                .and(authorContains(request.author()))
                .and(categoryContains(request.category()))
                .and(publisherContains(request.publisher()))
                .and(publicationYearEquals(request.publicationYear()));
    }

    private static Specification<Book> isbnContains(String isbn) {
        return (root, query, cb) ->
                isbn == null || isbn.isBlank()
                        ? null
                        : cb.like(
                        cb.lower(root.get("isbn")),
                        "%" + isbn.toLowerCase() + "%"
                );
    }

    private static Specification<Book> titleContains(String title) {
        return (root, query, cb) ->
                title == null || title.isBlank()
                        ? null
                        : cb.like(
                        cb.lower(root.get("title")),
                        "%" + title.toLowerCase() + "%"
                );
    }

    private static Specification<Book> publisherContains(String publisher) {
        return (root, query, cb) ->
                publisher == null || publisher.isBlank()
                        ? null
                        : cb.like(
                        cb.lower(root.get("publisher")),
                        "%" + publisher.toLowerCase() + "%"
                );
    }

    private static Specification<Book> publicationYearEquals(Short year) {
        return (root, query, cb) ->
                year == null
                        ? null
                        : cb.equal(root.get("publicationYear"), year);
    }

    private static Specification<Book> authorContains(String author) {

        return (root, query, cb) -> {

            if (author == null || author.isBlank())
                return null;

            Join<Book, Author> join = root.join("author");

            return cb.like(
                    cb.lower(join.get("name")),
                    "%" + author.toLowerCase() + "%"
            );
        };
    }

    private static Specification<Book> categoryContains(String category) {

        return (root, query, cb) -> {

            if (category == null || category.isBlank())
                return null;

            Join<Book, Category> join = root.join("category");

            return cb.like(
                    cb.lower(join.get("name")),
                    "%" + category.toLowerCase() + "%"
            );
        };
    }

}
