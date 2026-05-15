package com.example.BookApplication.dto.book;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookUpdateDTO {

    @Schema(example = "The great pirate era")
    @JsonProperty("title")
    private String title;

    @Schema(example = "G.Figarland")
    @JsonProperty("author")
    private String author;

    @Schema(example = "Fiction")
    @JsonProperty("genre")
    private String genre;

}
