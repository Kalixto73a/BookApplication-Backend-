package com.example.BookApplication.mapper.book;

import com.example.BookApplication.dto.book.BookDTO;
import com.example.BookApplication.entity.BookEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {
    BookDTO toBookDTO(BookEntity bookEntity);

    BookEntity toBook(BookDTO bookDTO);

    List<BookDTO> toBookDTOList(List<BookEntity> bookEntityList);

    List<BookEntity> toBookList(List<BookDTO> bookDTOList);

}
