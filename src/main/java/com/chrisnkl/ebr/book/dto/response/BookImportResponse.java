package com.chrisnkl.ebr.book.dto.response;

import java.util.List;

public record BookImportResponse(

        int totalRows,
        int importedBooks,
        int failedBooks,
        long durationMs,
        List<String> errors
) { }
