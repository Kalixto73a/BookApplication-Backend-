package com.example.BookApplication.auth.service.impl;

import com.example.BookApplication.auth.service.JwtService;
import com.example.BookApplication.config.initializeValues.DataInitializer;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtServiceImpl.class);

    @Value("${application.security.jwt.secret-key}")
    private String SECRET_KEY;

    @Value("${application.security.jwt.expiration}")
    private long TOKEN_EXPIRATION;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long REFRESH_WINDOW;

    @Override
    public String generateToken(UserDetails userDetails){

        LOGGER.debug("Generating token");

        Map<String, Object> claims = Map.of("authorities", userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList());

        LOGGER.debug("Token generated successfully");

        return generateToken(claims, userDetails.getUsername());

    }

    @Override
    public String generateToken(Map<String, Object> claims, String subject){
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims getAllClaims(String token){

        try {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e){
            return e.getClaims();
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e){
            throw new RuntimeException("Invalid JWT token or mal formed", e);
        }

    }

    private <T> T getClaim(String token, Function<Claims, T> claimsMapper){
        Claims allClaims = getAllClaims(token);
        return claimsMapper.apply(allClaims);
    }

    @Override
    public String getUsername(String token) {
        return getClaim(token, Claims::getSubject);
    }

    @Override
    public Date getExpirationDate(String token){
        return getClaim(token, Claims::getExpiration);
    }

    @Override
    public boolean isTokenExpired(String token){
        return getExpirationDate(token).before(new Date());
    }

    @Override
    public boolean canBeTokenRenewed(String token){
        return getExpirationDate(token).before(new Date(System.currentTimeMillis() + REFRESH_WINDOW));
    }

    @Override
    public String renewToken(String token, UserDetails userDetails){
        if (!canBeTokenRenewed(token)){
            throw new RuntimeException("Token cannot be renewed");
        }
        return generateToken(userDetails);
    }

    @Override
    public boolean isValidToken(String token, UserDetails userDetails){
        String username = getUsername(token);
        return username.equals(userDetails.getUsername());
    }

    @Override
    public String generateRefreshToken(){
        return UUID.randomUUID().toString();
    }

}
