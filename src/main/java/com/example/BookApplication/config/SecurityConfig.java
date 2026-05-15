package com.example.BookApplication.config;

import com.example.BookApplication.auth.filters.JwtFilters;
import com.example.BookApplication.dto.user.TokenResponseDTO;
import com.example.BookApplication.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilters jwtFilters;
    private final AuthenticationProvider authenticationProvider;
    private final UserService userService;
    private final CorsConfigurationSource corsConfigurationSource;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/proxy/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2Login(oauth2 -> oauth2
                        .successHandler((request, response, authentication) -> {
                            TokenResponseDTO token = userService.loginWithGithubDTO(authentication);
                            response.setContentType("application/json");
                            response.getWriter().write(
                                    "{\"token\":\"" + token.getToken() + "\",\"refreshToken\":\"" + token.getRefreshToken() + "\"}"
                            );
                        })
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtFilters, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
