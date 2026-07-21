package com.chrisnkl.ebr.batch.writer;

import com.chrisnkl.ebr.book.entity.Book;
import com.chrisnkl.ebr.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class BookItemWriter implements ItemWriter<Book> {

    private final BookRepository bookRepository;

    @Override
    public void write(@NonNull Chunk<? extends Book> chunk) throws Exception {
        log.info("Writing {} books", chunk.size());
        bookRepository.saveAll(chunk.getItems());
    }
}
