package com.chrisnkl.ebr.book.service;

import com.chrisnkl.ebr.book.entity.Category;
import com.chrisnkl.ebr.book.exception.CategoryCreationFailureException;
import com.chrisnkl.ebr.book.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;


    public Category findOrCreate(String name) {

        log.info("CategoryService.findOrCreate is called with name: {}", name);

        try {
            return categoryRepository.findByName(name)
                    .orElseGet(() ->
                            categoryRepository.save(
                                    Category.builder()
                                            .name(name)
                                            .build()
                            )
                    );
        } catch (Exception e) {
            log.error("Error occurred while creating category: {}", name);
            throw new CategoryCreationFailureException("Failed to create category: " + name, e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
