package com.urban.urban_move.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Custom authentication success handler for form login.
 * Issues a JWT token and stores it in a cookie.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    
    private final JwtTokenProvider tokenProvider;
    
    @Value("${app.jwt.cookie.name:Authorization}")
    private String tokenCookieName;
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                       HttpServletResponse response,
                                       Authentication authentication) throws IOException, ServletException {
        
        String token = tokenProvider.generateToken(authentication);
        
        // Create cookie with JWT token
        Cookie cookie = new Cookie(tokenCookieName, token);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // Set to true in production with HTTPS
        cookie.setMaxAge(86400); // 24 hours
        
        response.addCookie(cookie);
        
        log.info("User {} authenticated successfully, token stored in cookie", authentication.getName());
        
        // Redirect to dashboard
        response.sendRedirect("/dashboard");
    }
}
