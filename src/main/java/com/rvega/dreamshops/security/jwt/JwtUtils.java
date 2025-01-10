package com.rvega.dreamshops.security.jwt;

import com.rvega.dreamshops.security.user.ShopUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;

/**
 * Utility class for handling JWT (JSON Web Tokens) related operations.
 * This class provides methods for generating a JWT token for a user, extracting the username from a token,
 * and validating a token.
 */
@Component
public class JwtUtils {
    /**
     * Secret key used for signing and verifying JWT tokens.
     * This value is loaded from the application properties file using the @Value annotation.
     */
    @Value("${auth.token.jwtSecret}")
    private String jwtSecret;

    /**
     * Expiration time for JWT tokens in milliseconds.
     * This value is loaded from the application properties file using the @Value annotation.
     */
    @Value("${auth.token.expirationInMils}")
    private int expirationTime;

    /**
     * Generates a JWT token for the given user authentication.
     *
     * @param authentication The user authentication object containing user details.
     * @return A JWT token string.
     */
    public String generateTokenForUser(Authentication authentication) {
        ShopUserDetails userPrincipal = (ShopUserDetails) authentication.getPrincipal();
        List<String> roles = userPrincipal.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority).toList();

        return Jwts.builder()
                .setSubject(userPrincipal.getEmail())
                .claim("id", userPrincipal.getId())
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + expirationTime))
                .signWith(key(),  SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Private method to generate a key for signing and verifying JWT tokens.
     *
     * @return A key object.
     */
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    /**
     * Extracts the username from the given JWT token.
     *
     * @param token The JWT token string.
     * @return The username extracted from the token.
     */
    public String getUsernameFromToken(String token) {
        return Jwts.parser()
               .setSigningKey(key())
               .build()
               .parseClaimsJws(token)
               .getBody()
               .getSubject();
    }

    /**
     * Validates the given JWT token.
     *
     * @param token The JWT token string.
     * @return True if the token is valid, false otherwise.
     * @throws JwtException If the token is invalid or expired.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(token);

            return true;
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | IllegalArgumentException |
                 io.jsonwebtoken.security.SignatureException e) {
            throw new JwtException(e.getMessage());
        }
    }
}
