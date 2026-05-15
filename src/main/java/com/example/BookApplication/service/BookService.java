package com.example.BookApplication.service;

import com.example.BookApplication.dto.book.BookCreateDTO;
import com.example.BookApplication.dto.book.BookDTO;
import com.example.BookApplication.dto.book.BookUpdateDTO;
import com.example.BookApplication.entity.BookEntity;

import java.util.List;

public interface BookService {

    List<BookEntity> getAllBooks();
    List<BookDTO> getAllBooksDTO();

    BookEntity getBookById(Integer id);
    BookDTO getBookByIdDTO(Integer id);

    BookEntity addBook(BookCreateDTO book);
    BookDTO addBookDTO(BookCreateDTO book);

    BookEntity updateBookById(Integer id, BookUpdateDTO book);
    BookDTO updateBookByIdDTO(Integer id, BookUpdateDTO book);

    BookEntity deleteBookById(Integer id);
    BookDTO deleteBookByIdDTO(Integer id);

}
