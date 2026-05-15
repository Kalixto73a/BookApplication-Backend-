package com.example.BookApplication.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequestDTO {

    @Schema(example = "test@gmail.com")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Email must have a valid format")
    @NotBlank(message = "email is required")
    @JsonProperty("email")
    private String email;

    @Schema(example = "password123")
    @NotBlank(message = "password is required")
    @JsonProperty("password")
    @Size(min = 8, message = "password must be at least 8 characters")
    private String password;

    @Schema(example = "Your")
    @NotBlank(message = "firstName is required")
    @JsonProperty("firstName")
    private String firstName;

    @Schema(example = "Name")
    @NotBlank(message = "lastName is required")
    @JsonProperty("lastName")
    private String lastName;

}
