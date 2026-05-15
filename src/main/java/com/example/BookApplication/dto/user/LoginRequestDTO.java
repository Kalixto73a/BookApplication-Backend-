package com.example.BookApplication.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class LoginRequestDTO {

    @Schema(example = "test@gmail.com")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Email must have a valid format")
    @NotBlank(message = "email is required")
    @JsonProperty("email")
    private String email;

    @Schema(example = "password123")
    @NotBlank(message = "password is required")
    @JsonProperty("password")
    private String password;

}
