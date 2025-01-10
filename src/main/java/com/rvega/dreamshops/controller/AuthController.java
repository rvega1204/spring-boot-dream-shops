package com.rvega.dreamshops.controller;

import com.rvega.dreamshops.request.LoginRequest;
import com.rvega.dreamshops.response.ApiResponse;
import com.rvega.dreamshops.response.JwtResponse;
import com.rvega.dreamshops.security.jwt.JwtUtils;
import com.rvega.dreamshops.security.user.ShopUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

/**
 * Controller responsible for handling authentication-related requests.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/auth")
public class AuthController {

    /**
     * The authentication manager used to authenticate users.
     */
    private final AuthenticationManager authenticationManager;

    /**
     * Utility class for generating JWT tokens.
     */
    private final JwtUtils jwtUtils;

    /**
     * Handles a login request.
     *
     * @param request The login request containing the user's email and password.
     * @return A ResponseEntity containing an ApiResponse with a success message and a JwtResponse if the login is successful.
     *         If the login fails, an ApiResponse with an error message is returned.
     */
    @RequestMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            // Authenticate the user using the provided email and password
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            request.getEmail(), request.getPassword()));

            // Set the authenticated user in the security context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generate a JWT token for the authenticated user
            String jwt = jwtUtils.generateTokenForUser(authentication);

            // Get the user details from the authentication principal
            ShopUserDetails userDetails = (ShopUserDetails) authentication.getPrincipal();

            // Create a JwtResponse object with the user's ID and the generated JWT token
            JwtResponse jwtResponse = new JwtResponse(userDetails.getId(), jwt);

            // Return a successful response with the ApiResponse and JwtResponse
            return ResponseEntity.ok(new ApiResponse("Logged in successfully", jwtResponse));
        } catch (AuthenticationException e) {
            // Return an unauthorized response with the error message from the AuthenticationException
            return ResponseEntity.status(UNAUTHORIZED).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
