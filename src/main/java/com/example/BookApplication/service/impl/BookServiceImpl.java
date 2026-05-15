package com.example.BookApplication.service.impl;

import com.example.BookApplication.dto.book.BookCreateDTO;
import com.example.BookApplication.dto.book.BookDTO;
import com.example.BookApplication.dto.book.BookUpdateDTO;
import com.example.BookApplication.entity.BookEntity;
import com.example.BookApplication.mapper.book.BookMapper;
import com.example.BookApplication.repository.BookRepository;
import com.example.BookApplication.service.BookService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookServiceImpl.class);

    @Autowired
    private final BookMapper bookMapper;

    @Autowired
    private final BookRepository bookRepository;

    @Override
    public List<BookEntity> getAllBooks(){
        List<BookEntity> listOfBookEntities = bookRepository.findAll();

        LOGGER.debug("Books obtained {}", listOfBookEntities.size());

        return listOfBookEntities;
    }

    @Override
    public List<BookDTO> getAllBooksDTO(){
        List<BookEntity> listOfBookEntities = getAllBooks();

        List<BookDTO> listOfBooksDTO = bookMapper.toBookDTOList(listOfBookEntities);

        LOGGER.debug("Books mapped correctly");

        return listOfBooksDTO;
    }

    @Override
    public BookEntity getBookById(Integer id){
        BookEntity bookEntity = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id: " + id + " not found"));

        LOGGER.debug("Book obtained with id: {}", id);

        return bookEntity;
    }

    @Override
    public BookDTO getBookByIdDTO(Integer id){
        BookEntity bookEntity = getBookById(id);

        BookDTO bookDTO = bookMapper.toBookDTO(bookEntity);

        LOGGER.debug("Book mapped correctly");

        return bookDTO;
    }

    @Override
    public BookEntity addBook(BookCreateDTO bookCreate){
        BookEntity bookEntity = new BookEntity();
        bookEntity.setTitle(bookCreate.getTitle());
        bookEntity.setAuthor(bookCreate.getAuthor());
        bookEntity.setGenre(bookCreate.getGenre());

        bookRepository.save(bookEntity);

        LOGGER.debug("Book create with id: {}", bookEntity.getId());

        return bookEntity;
    }

    @Override
    public BookDTO addBookDTO(BookCreateDTO bookCreate){
        BookEntity bookEntityAdded = addBook(bookCreate);

        BookDTO bookAddedDTO = bookMapper.toBookDTO(bookEntityAdded);

        LOGGER.debug("Added Book mapped correctly");

        return bookAddedDTO;
    }

    @Override
    public BookEntity updateBookById(Integer id, BookUpdateDTO bookUpdate){

        String title = bookUpdate.getTitle();
        String author = bookUpdate.getAuthor();
        String genre = bookUpdate.getGenre();

        BookEntity bookEntity = getBookById(id);
        if (title != null) bookEntity.setTitle(bookUpdate.getTitle());
        if (author != null) bookEntity.setAuthor(bookUpdate.getAuthor());
        if (genre != null) bookEntity.setGenre(bookUpdate.getGenre());

        bookRepository.save(bookEntity);

        LOGGER.debug("Book with id: {} was updated", id);

        return bookEntity;
    }

    @Override
    public BookDTO updateBookByIdDTO(Integer id, BookUpdateDTO bookUpdate){
        BookEntity updatedBookEntity = updateBookById(id, bookUpdate);

        BookDTO updatedBookDTO = bookMapper.toBookDTO(updatedBookEntity);

        LOGGER.debug("Updated Book mapped correctly");

        return updatedBookDTO;
    }

    @Override
    public BookEntity deleteBookById(Integer id){
        BookEntity bookEntityToDelete = getBookById(id);

        bookRepository.deleteById(id);

        LOGGER.debug("Book with id: {} was deleted successfully", id);

        return bookEntityToDelete;
    }

    @Override
    public BookDTO deleteBookByIdDTO(Integer id){
        BookEntity bookEntityToDelete = deleteBookById(id);

        BookDTO bookToDeleteDTO = bookMapper.toBookDTO(bookEntityToDelete);

        LOGGER.debug("Book is going to be deleted mapped correctly");

        return bookToDeleteDTO;
    }

}
