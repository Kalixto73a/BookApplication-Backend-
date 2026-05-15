package com.example.BookApplication.auth.filters;

import com.example.BookApplication.auth.service.impl.JwtServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilters extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtFilters.class);

    @Autowired
    private final JwtServiceImpl jwtServiceImpl;

    @Autowired
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer ")){
            LOGGER.error("No token found");
            filterChain.doFilter(request,response);
            return;
        }

        String token = authorization.substring(7);

        boolean isTokenExpired = jwtServiceImpl.isTokenExpired(token);

        boolean canBeTokenRenewed = jwtServiceImpl.canBeTokenRenewed(token);

        if (isTokenExpired && !canBeTokenRenewed){
            LOGGER.error("Token expired");
            filterChain.doFilter(request, response);
            return;
        }

        String username = jwtServiceImpl.getUsername(token);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        boolean isValidToken = jwtServiceImpl.isValidToken(token, userDetails);

        if (!isValidToken || SecurityContextHolder.getContext().getAuthentication() != null){
            LOGGER.error("Invalid token or user already authenticated");
            filterChain.doFilter(request, response);
            return;
        }

        if (isTokenExpired){
            String renewToken = jwtServiceImpl.renewToken(token, userDetails);
            response.setHeader("Authorization", "Bearer " + renewToken);
        }

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/api/auth/") ||
                path.startsWith("/v3/api-docs") ||
                path.startsWith("/swagger-ui") ||
                path.equals("/swagger-ui.html") ||
                path.startsWith("/login/oauth2") ||
                path.startsWith("/oauth2");
    }

}
