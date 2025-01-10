package com.rvega.dreamshops.security.config;

import com.rvega.dreamshops.security.jwt.AuthTokenFilter;
import com.rvega.dreamshops.security.jwt.JwtAuthEntryPoint;
import com.rvega.dreamshops.security.user.ShopUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class ShopConfig {

    // Service for retrieving user details
    private final ShopUserDetailsService userDetailsService;

    // Entry point for handling authentication exceptions
    private final JwtAuthEntryPoint jwtAuthEntryPoint;

    // List of URLs that require authentication
    private static final List<String> SECURED_URLS = List.of("/api/v1/cart/**", "/api/v1/cartItems/**");

    // Bean for creating an instance of ModelMapper
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    // Bean for creating an instance of BCryptPasswordEncoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Bean for creating an instance of AuthTokenFilter
    @Bean
    public AuthTokenFilter authTokenFilter() {
        return new AuthTokenFilter();
    }

    // Bean for creating an instance of AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // Bean for creating an instance of DaoAuthenticationProvider
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        var authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // Method for configuring security filters and settings
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable) // Disable CSRF protection
                .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthEntryPoint)) // Set authentication entry point
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Set session creation policy
                .authorizeHttpRequests(auth -> auth.requestMatchers(SECURED_URLS.toArray(String[]::new)).authenticated() // Set URL-based authorization
                        .anyRequest().permitAll()); // Allow access to any other requests

        http.authenticationProvider(daoAuthenticationProvider()); // Set authentication provider
        http.addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class); // Add AuthTokenFilter before UsernamePasswordAuthenticationFilter

        return http.build(); // Build and return the SecurityFilterChain
    }
}
