package com.example.BookApplication.controller;

import com.example.BookApplication.dto.book.BookCreateDTO;
import com.example.BookApplication.dto.book.BookDTO;
import com.example.BookApplication.dto.book.BookUpdateDTO;
import com.example.BookApplication.service.impl.BookServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Books", description = "Endpoints for managing books")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/book")
@SecurityRequirement(name = "Bearer Authentication")
public class BookController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookController.class);

    @Autowired
    private BookServiceImpl bookService;

    @Operation(summary = "Get all books", description = "Returns a list of all books")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<List<BookDTO>> getAllBook(){
        LOGGER.info("GET /api/book | Get all books");

        List<BookDTO> books = bookService.getAllBooksDTO();

        return ResponseEntity.ok(books);
    }

    @Operation(summary = "Get a book by id", description = "Returns a book by his id")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<BookDTO> getBookById(@PathVariable("id") Integer id){
        LOGGER.info("GET /api/book/{} | Get book by his id", id);

        BookDTO book = bookService.getBookByIdDTO(id);

        return ResponseEntity.ok(book);
    }

    @Operation(summary = "Add a book", description = "Returns the book you added whit his id")
    @PostMapping(value = "/addBook", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<BookDTO> addBook(@RequestBody @Valid BookCreateDTO bookCreated){
        LOGGER.info("POST /api/addBook | Creates a book");

        BookDTO savedBook = bookService.addBookDTO(bookCreated);

        return ResponseEntity.ok(savedBook);
    }

    @Operation(summary = "Edit the field you want of a book by his id", description = "Returns the book with his updated information")
    @PutMapping(value = "/editBook/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<BookDTO> updateBookById(@PathVariable("id") Integer id, @Valid @RequestBody BookUpdateDTO bookUpdated){
        LOGGER.info("PUT /api/editBook/{} | Edit a existing book", id);

        BookDTO editedBook = bookService.updateBookByIdDTO(id, bookUpdated);

        return ResponseEntity.ok(editedBook);
    }

    @Operation(summary = "Delete a book by his id", description = "Returns the information of the book you deleted")
    @DeleteMapping(value = "/deleteBook/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<BookDTO> deleteBookById(@PathVariable("id") Integer id){
        LOGGER.info("DELETE /api/deleteBook/{} | Delete book by his id", id);

        BookDTO deletedBook = bookService.deleteBookByIdDTO(id);

        return ResponseEntity.ok(deletedBook);
    }

}
