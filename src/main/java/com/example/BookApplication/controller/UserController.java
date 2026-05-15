package com.example.BookApplication.controller;

import com.example.BookApplication.dto.user.LoginRequestDTO;
import com.example.BookApplication.dto.user.RefreshTokenRequestDTO;
import com.example.BookApplication.dto.user.RegisterRequestDTO;
import com.example.BookApplication.dto.user.TokenResponseDTO;
import com.example.BookApplication.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication", description = "Endpoints for user authentication")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final UserServiceImpl userService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody @Valid LoginRequestDTO loginRequestDTO){

        LOGGER.info("POST /api/auth/login | Login to get your tokens");

        return ResponseEntity.ok(userService.loginDTO(loginRequestDTO));

    }

    @PostMapping("/register")
    public ResponseEntity<TokenResponseDTO> register(@RequestBody @Valid RegisterRequestDTO registerRequestDTO){

        LOGGER.info("POST /api/auth/register | Register an account and get your tokens");

        return ResponseEntity.ok(userService.registerDTO(registerRequestDTO));

    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponseDTO> refresh(@RequestBody @Valid RefreshTokenRequestDTO refreshTokenRequestDTO){

        LOGGER.info("POST /api/auth/refresh | Refresh your token with the refreshToken");

        return ResponseEntity.ok(userService.refreshTokenDTO(refreshTokenRequestDTO));

    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody @Valid RefreshTokenRequestDTO refreshTokenRequestDTO) {

        LOGGER.info("POST /api/auth/logout | Logout the user with his refreshToken");

        userService.logoutDTO(refreshTokenRequestDTO);

        return ResponseEntity.ok().build();

    }

}
