package com.rvega.dreamshops.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * This class serves as an entry point for unauthenticated requests. It handles the response when a user
 * attempts to access a secured resource without providing a valid JWT token.
 *
 * @author Tabnine Protected
 */
@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

    /**
     * This method is called when an unauthenticated request is made to a secured resource. It sends a JSON
     * response with an "Unauthorized" error message and sets the HTTP status code to 401 (Unauthorized).
     *
     * @param request The HTTP request that triggered the entry point.
     * @param response The HTTP response to be sent to the client.
     * @param authException The exception that caused the entry point to be called.
     * @throws IOException If an I/O error occurs while writing to the response.
     * @throws ServletException If a servlet-specific error occurs.
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        final Map<String, Object> body = new HashMap<>();
        body.put("error", "Unauthorized");
        //body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        body.put("message", "You may log in and try again.");
        //body.put("path", request.getServletPath());

        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);
    }
}
