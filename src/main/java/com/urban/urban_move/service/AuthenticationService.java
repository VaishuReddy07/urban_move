package com.urban.urban_move.service;

import com.urban.urban_move.domain.User;
import com.urban.urban_move.dto.AuthResponse;
import com.urban.urban_move.dto.LoginRequest;
import com.urban.urban_move.dto.RegisterRequest;
import com.urban.urban_move.dto.UserDTO;
import com.urban.urban_move.exception.AuthenticationException;
import com.urban.urban_move.exception.ResourceAlreadyExistsException;
import com.urban.urban_move.repository.UserRepository;
import com.urban.urban_move.security.CustomUserDetails;
import com.urban.urban_move.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Authentication service for user registration, login, and token management.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    
    /**
     * Register a new user.
     */
    @Transactional
    public UserDTO register(RegisterRequest registerRequest) {
        log.info("Registering user: {}", registerRequest.getEmail());
        
        // Check if user already exists
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            log.warn("Email already registered: {}", registerRequest.getEmail());
            throw new ResourceAlreadyExistsException("Email already registered");
        }
        
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            log.warn("Username already taken: {}", registerRequest.getUsername());
            throw new ResourceAlreadyExistsException("Username already taken");
        }
        
        // Create new user
        User user = User.builder()
            .username(registerRequest.getUsername())
            .email(registerRequest.getEmail())
            .password(passwordEncoder.encode(registerRequest.getPassword()))
            .fullName(registerRequest.getFullName())
            .phoneNumber(registerRequest.getPhoneNumber())
            .role(User.UserRole.RIDER)
            .emailVerified(false)
            .accountLocked(false)
            .loginAttempts(0L)
            .status(User.UserStatus.ACTIVE)
            .isActive(true)
            .build();
        
        User savedUser = userRepository.save(user);
        log.info("User registered successfully: {}", savedUser.getId());
        
        return userService.convertToDTO(savedUser);
    }
    
    /**
     * Authenticate user and generate JWT token.
     */
    public AuthResponse login(LoginRequest loginRequest) {
        log.info("Login attempt for user: {}", loginRequest.getUsername());
        
        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
                )
            );
            
            // Get authenticated user
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = userRepository.findByUsernameOrEmail(userDetails.getUsername(), userDetails.getUsername())
                .orElseThrow(() -> new AuthenticationException("User not found"));
            
            // Reset login attempts on successful login
            user.setLoginAttempts(0L);
            userRepository.save(user);
            
            // Generate JWT token
            String token = jwtTokenProvider.generateToken(authentication);
            
            log.info("User logged in successfully: {}", userDetails.getUsername());
            
            return AuthResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .expiresIn(86400L) // 24 hours in seconds
                .user(userService.convertToDTO(user))
                .build();
        } catch (Exception e) {
            log.error("Authentication failed for user: {}", loginRequest.getUsername(), e);
            throw new AuthenticationException("Invalid username or password");
        }
    }
    
    /**
     * Refresh JWT token.
     */
    public String refreshToken(String token) {
        log.info("Refreshing token");
        
        try {
            // Validate existing token
            if (!jwtTokenProvider.validateToken(token)) {
                throw new AuthenticationException("Invalid or expired token");
            }
            
            // Get username from token
            String username = jwtTokenProvider.getUsernameFromToken(token);
            if (username == null) {
                throw new AuthenticationException("Cannot extract username from token");
            }
            
            // Generate new token
            String newToken = jwtTokenProvider.generateTokenFromUsername(username);
            log.info("Token refreshed successfully for user: {}", username);
            
            return newToken;
        } catch (Exception e) {
            log.error("Token refresh failed", e);
            throw new AuthenticationException("Token refresh failed: " + e.getMessage());
        }
    }
}
