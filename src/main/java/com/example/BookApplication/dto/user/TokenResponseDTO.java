package com.example.BookApplication.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TokenResponseDTO {

    @NotBlank
    @JsonProperty("token")
    private String token;

    @NotBlank
    @JsonProperty("refreshToken")
    private String refreshToken;

}
