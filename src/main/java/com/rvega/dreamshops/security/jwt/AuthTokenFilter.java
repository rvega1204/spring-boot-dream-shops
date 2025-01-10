package com.rvega.dreamshops.security.jwt;

import com.rvega.dreamshops.security.user.ShopUserDetailsService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * This class is a filter that intercepts incoming HTTP requests and validates JWT tokens.
 * It extends Spring's {@link OncePerRequestFilter} and overrides the {@link #doFilterInternal} method.
 *
 * The filter performs the following tasks:
 * 1. Extracts the JWT token from the request's Authorization header.
 * 2. Validates the JWT token using the {@link JwtUtils} class.
 * 3. If the token is valid, retrieves the username from the token and loads the user's details using the
 *    {@link ShopUserDetailsService}.
 * 4. Creates a {@link UsernamePasswordAuthenticationToken} with the user's details and sets it in the
 *    {@link SecurityContextHolder}.
 * 5. If any exceptions occur during the token validation or user details loading, it sends an appropriate
 *    HTTP response with an error message.
 * 6. Finally, it allows the request to continue to the next filter in the chain.
 */
public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private ShopUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            if (StringUtils.hasText(jwt) && jwtUtils.validateToken(jwt)) {
                String username = jwtUtils.getUsernameFromToken(jwt);
                UserDetails userDetails= userDetailsService.loadUserByUsername(username);
                var auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (JwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(e.getMessage() + " : Invalid or expired token");
            return;
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(e.getMessage());
            return;
        }

        filterChain.doFilter(request, response);

    }

    /**
     * Extracts the JWT token from the request's Authorization header.
     *
     * @param request The incoming HTTP request.
     * @return The extracted JWT token or null if the header is not present or does not start with "Bearer ".
     */
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }
}
