package com.example.BookApplication.dto.book;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookCreateDTO {

    @Schema(example = "The Great Gatsby")
    @NotBlank(message = "title is required")
    @JsonProperty("title")
    private String title;

    @Schema(example = "F. Scott Fitzgerald")
    @NotBlank(message = "author is required")
    @JsonProperty("author")
    private String author;

    @Schema(example = "Terror")
    @NotBlank(message = "genre is required")
    @JsonProperty("genre")
    private String genre;

}
