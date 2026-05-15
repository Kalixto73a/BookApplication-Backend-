package com.example.BookApplication.service;

import com.example.BookApplication.dto.user.LoginRequestDTO;
import com.example.BookApplication.dto.user.RefreshTokenRequestDTO;
import com.example.BookApplication.dto.user.RegisterRequestDTO;
import com.example.BookApplication.dto.user.TokenResponseDTO;
import com.example.BookApplication.entity.UserEntity;
import org.springframework.security.core.Authentication;

public interface UserService {

    UserEntity register(RegisterRequestDTO registerRequestDTO);

    TokenResponseDTO registerDTO(RegisterRequestDTO registerRequestDTO);

    UserEntity login(LoginRequestDTO loginRequestDTO);

    TokenResponseDTO loginDTO(LoginRequestDTO loginRequestDTO);

    UserEntity refreshToken(RefreshTokenRequestDTO refreshTokenRequestDTO);

    TokenResponseDTO refreshTokenDTO(RefreshTokenRequestDTO refreshTokenRequestDTO);

    TokenResponseDTO loginWithGithub(Authentication authentication);

}
