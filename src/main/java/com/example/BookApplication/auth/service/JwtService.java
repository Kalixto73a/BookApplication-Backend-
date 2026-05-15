package com.example.BookApplication.auth.service;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.Map;

public interface JwtService {

    String generateToken(UserDetails userDetails);

    String generateToken(Map<String, Object> claims, String subject);

    String getUsername(String token);

    Date getExpirationDate(String token);

    boolean isTokenExpired(String token);

    boolean canBeTokenRenewed(String token);

    String renewToken(String token, UserDetails userDetails);

    boolean isValidToken(String token, UserDetails userDetails);

    String generateRefreshToken();

}
