package com.example.BookApplication.config.initializeValues;

import com.example.BookApplication.controller.BookController;
import com.example.BookApplication.entity.BookEntity;
import com.example.BookApplication.entity.UserEntity;
import com.example.BookApplication.enums.UserRole;
import com.example.BookApplication.repository.BookRepository;
import com.example.BookApplication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final BookRepository bookRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {

        LOGGER.info("Starting seeding the database");

        if (!userRepository.existsByEmail("admin@admin.com")) {
            UserEntity admin = UserEntity.builder()
                    .firstName("Admin")
                    .lastName("Admin")
                    .email("admin@admin.com")
                    .password(passwordEncoder.encode("admin"))
                    .role(UserRole.ADMIN)
                    .build();
            userRepository.save(admin);
        }

        if (bookRepository.count() == 0) {
            List<BookEntity> books = List.of(
                    createBook("One Piece", "Eiichirō Oda", "Fiction"),
                    createBook("Chainsaw Man", "Tatsuki Fujimoto", "Fiction"),
                    createBook("Sword Art Online", "Reki Kawahara", "Fiction")
            );
            bookRepository.saveAll(books);
        }

        LOGGER.info("Database successfully seeded");

    }

    private BookEntity createBook(String title, String author, String genre) {
        BookEntity book = new BookEntity();
        book.setTitle(title);
        book.setAuthor(author);
        book.setGenre(genre);
        return book;
    }
}
