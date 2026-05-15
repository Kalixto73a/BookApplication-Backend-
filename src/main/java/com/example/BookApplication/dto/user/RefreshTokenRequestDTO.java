package com.example.BookApplication.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RefreshTokenRequestDTO {

    @Schema(description = "Refresh token obtained from login or register")
    @NotBlank(message = "Refresh token is required")
    private String refreshToken;

}
