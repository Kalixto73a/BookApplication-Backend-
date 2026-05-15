package com.example.BookApplication.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "book")
public class BookEntity {

    @Id
    @GeneratedValue
    @Column(name = "id", unique = true, nullable = false, updatable = false)
    private Integer id;

    @Column(name = "title", unique = false, nullable = true, updatable = true)
    private String title;

    @Column(name = "author", unique = false, nullable = true, updatable = true)
    private String author;

    @Column(name = "genre", unique = false, nullable = true, updatable = true)
    private String genre;

}
