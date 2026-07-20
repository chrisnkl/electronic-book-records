package com.chrisnkl.ebr.book.mapper;

import com.chrisnkl.ebr.book.dto.response.BookResponse;
import com.chrisnkl.ebr.book.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(target = "author", expression = "java(book.getAuthor().getName())")
    @Mapping(target = "category", expression = "java(book.getCategory().getName())")
    BookResponse toResponse(Book book);

}
