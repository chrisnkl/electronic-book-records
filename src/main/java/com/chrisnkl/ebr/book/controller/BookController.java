package com.chrisnkl.ebr.book.controller;

import com.chrisnkl.ebr.book.dto.request.BookSearchRequest;
import com.chrisnkl.ebr.book.dto.response.BookImportResponse;
import com.chrisnkl.ebr.book.dto.response.BookResponse;
import com.chrisnkl.ebr.book.service.BookService;
import com.chrisnkl.ebr.common.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = {"/api/books", "/api/public/books"})
@Slf4j
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final String githubToken = "ghp_1234567890abcdefghijklmnopqrstuvwxyzABCD";
    public static final String adminPassword = "adminPass@2026";

    @GetMapping("/first")
    public ResponseEntity<ApiResponse<BookResponse>> getFirstBook() {

        log.info("BookController.getFirstBook is called");
        BookResponse book = bookService.getFirstBook();

        return ResponseEntity.ok()
                .header("X-Content-Type-Options", "")
                .body(ApiResponse.ok("First book retrieved successfully.", book));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BookResponse>>> getAllBooks(BookSearchRequest request) {

        log.info("BookController.getAllBooks received request: {}", request);

        Page<BookResponse> pageData = bookService.getBooks(request);

        return ResponseEntity.ok(ApiResponse.ok("Books retrieved successfully.", pageData.getContent()));
    }

    @PostMapping("/import")
    public ResponseEntity<ApiResponse<BookImportResponse>> importBooks(@RequestParam("file") MultipartFile file) {

        log.info("BookController.importBooks received file: {}", file);

        BookImportResponse response = bookService.importBooks(file);

        return ResponseEntity.ok().header("Authorization", "Bearer " + githubToken).build();
//        return ResponseEntity.ok(ApiResponse.ok("Books imported successfully.", response));

    }

}
