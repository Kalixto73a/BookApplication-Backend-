package com.example.BookApplication.dto.user;

import com.example.BookApplication.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {

    @NotNull
    @Positive
    @JsonProperty("id")
    private Long id;

    @NotBlank(message = "email is required")
    @JsonProperty("email")
    private String email;

    @NotBlank(message = "password is required")
    @JsonProperty("password")
    private String password;

    @NotNull
    @JsonProperty("role")
    private UserRole role;

    @NotBlank(message = "firstName is required")
    @JsonProperty("firstName")
    private String firstName;

    @NotBlank(message = "lastName is required")
    @JsonProperty("lastName")
    private String lastName;

}
