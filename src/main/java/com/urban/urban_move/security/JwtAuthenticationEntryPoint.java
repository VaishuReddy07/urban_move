package com.urban.urban_move.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.urban.urban_move.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Entry point for JWT authentication errors.
 */
@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    
    @Override
    public void commence(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse,
                         AuthenticationException e) throws IOException, ServletException {
        
        log.error("Authentication error: {}", e.getMessage());
        
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        
        String timestamp = Instant.now()
            .atZone(ZoneId.of("UTC"))
            .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        
        ErrorResponse errorResponse = ErrorResponse.builder()
            .timestamp(timestamp)
            .status(HttpServletResponse.SC_UNAUTHORIZED)
            .error("Unauthorized")
            .errorCode("AUTHENTICATION_FAILED")
            .message(e.getMessage())
            .path(httpServletRequest.getServletPath())
            .build();
        
        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(httpServletResponse.getOutputStream(), errorResponse);
    }
}
