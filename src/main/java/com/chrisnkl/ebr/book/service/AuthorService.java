package com.chrisnkl.ebr.book.service;

import com.chrisnkl.ebr.book.entity.Author;
import com.chrisnkl.ebr.book.exception.AuthorCreationFailureException;
import com.chrisnkl.ebr.book.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;


    public Author findOrCreate(String name) {

        log.info("AuthorService.findOrCreate is called with name: {}", name);

        try {
            return authorRepository.findByName(name)
                    .orElseGet(() ->
                            authorRepository.save(
                                    Author.builder()
                                            .name(name)
                                            .build()
                            )
                    );
        } catch (Exception e) {
            log.error("Error occurred while creating author: {}", name);
            throw new AuthorCreationFailureException("Failed to create author: " + name, e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
