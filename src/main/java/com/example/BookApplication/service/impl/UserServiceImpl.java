package com.example.BookApplication.service.impl;

import com.example.BookApplication.auth.service.impl.JwtServiceImpl;
import com.example.BookApplication.dto.user.LoginRequestDTO;
import com.example.BookApplication.dto.user.RefreshTokenRequestDTO;
import com.example.BookApplication.dto.user.RegisterRequestDTO;
import com.example.BookApplication.dto.user.TokenResponseDTO;
import com.example.BookApplication.entity.UserEntity;
import com.example.BookApplication.enums.UserRole;
import com.example.BookApplication.exception.InvalidRefreshTokenException;
import com.example.BookApplication.exception.UserAlreadyExistsException;
import com.example.BookApplication.repository.UserRepository;
import com.example.BookApplication.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtServiceImpl jwtServiceImpl;

    @Override
    public UserEntity login(LoginRequestDTO loginRequestDTO) {

        UserEntity user = userRepository.findByEmail(loginRequestDTO.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("There is no account with that email"));

        if (!passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        LOGGER.debug("User logged in with email: {}", user.getEmail());

        return user;

    }

    @Override
    public TokenResponseDTO loginDTO(LoginRequestDTO loginRequestDTO) {

        UserEntity user = login(loginRequestDTO);

        String token = jwtServiceImpl.generateToken(user);

        saveRefreshToken(user);

        TokenResponseDTO response = new TokenResponseDTO();
        response.setToken(token);
        response.setRefreshToken(user.getRefreshToken());

        LOGGER.debug("User logged: {} successfully mapped", user.getEmail());

        return response;

    }

    @Override
    public UserEntity register(RegisterRequestDTO registerRequestDTO) {

        boolean existsByEmail = userRepository.existsByEmail(registerRequestDTO.getEmail());

        if (existsByEmail) {
            throw new UserAlreadyExistsException("That email is already registered");
        }

        UserEntity user = UserEntity.builder()
                .firstName(registerRequestDTO.getFirstName())
                .lastName(registerRequestDTO.getLastName())
                .email(registerRequestDTO.getEmail())
                .password(passwordEncoder.encode(registerRequestDTO.getPassword()))
                .role(UserRole.USER)
                .build();

        userRepository.save(user);

        LOGGER.debug("User registered with email: {}", user.getEmail());

        return user;
    }

    @Override
    public TokenResponseDTO registerDTO(RegisterRequestDTO registerRequestDTO) {

        UserEntity user = register(registerRequestDTO);

        String token = jwtServiceImpl.generateToken(user);

        saveRefreshToken(user);

        TokenResponseDTO response = new TokenResponseDTO();
        response.setToken(token);
        response.setRefreshToken(user.getRefreshToken());

        LOGGER.debug("User registered: {}, successfully mapped", user.getEmail());

        return response;

    }

    @Override
    public UserEntity refreshToken(RefreshTokenRequestDTO refreshTokenRequestDTO){

        UserEntity user = userRepository.findByRefreshToken(refreshTokenRequestDTO.getRefreshToken())
                .orElseThrow(() -> new InvalidRefreshTokenException("Invalid refresh token"));

        if (user.getRefreshTokenExpiresAt().isBefore(LocalDateTime.now())){
            throw new InvalidRefreshTokenException("Refresh token expired, pls login again");
        }

        LOGGER.debug("Token refreshed for user: {}", user.getEmail());

        return user;

    }

    @Override
    public TokenResponseDTO refreshTokenDTO(RefreshTokenRequestDTO refreshTokenRequestDTO){

        UserEntity user = refreshToken(refreshTokenRequestDTO);

        String newToken = jwtServiceImpl.generateToken(user);
        saveRefreshToken(user);

        TokenResponseDTO response = new TokenResponseDTO();
        response.setToken(newToken);
        response.setRefreshToken(user.getRefreshToken());

        LOGGER.debug("Refreshed token for user: {}, successfully mapped", user.getEmail());

        return response;

    }

    public TokenResponseDTO loginWithGithub(Authentication authentication){

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String login = oAuth2User.getAttribute("login");

        if (email == null){
            email = login + "@github.com";
        }

        if (name == null){
            name = login;
        }

        String finalEmail = email;
        String finalName = name;

        UserEntity user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    UserEntity newUser = UserEntity.builder()
                            .email(finalEmail)
                            .firstName(finalName)
                            .lastName("")
                            .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                            .role(UserRole.USER)
                            .build();
                    return userRepository.save(newUser);
                });
        String token = jwtServiceImpl.generateToken(user);
        saveRefreshToken(user);

        TokenResponseDTO response = new TokenResponseDTO();
        response.setToken(token);
        response.setRefreshToken(user.getRefreshToken());

        LOGGER.debug("User: {}, successfully logged in with github", user.getEmail());

        return response;

    }

    private void saveRefreshToken(UserEntity user){

        String refreshToken = jwtServiceImpl.generateRefreshToken();

        user.setRefreshToken(refreshToken);

        user.setRefreshTokenExpiresAt(LocalDateTime.now().plusDays(7));

        userRepository.save(user);

        LOGGER.debug("Token successfully refreshed");

    }

}
