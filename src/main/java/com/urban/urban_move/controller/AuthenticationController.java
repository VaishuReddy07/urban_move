package com.urban.urban_move.controller;

import com.urban.urban_move.dto.ApiResponse;
import com.urban.urban_move.dto.AuthResponse;
import com.urban.urban_move.dto.LoginRequest;
import com.urban.urban_move.dto.RegisterRequest;
import com.urban.urban_move.dto.UserDTO;
import com.urban.urban_move.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/**
 * Authentication REST API controller.
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Authentication", description = "Authentication and authorization endpoints")
@Slf4j
@RequiredArgsConstructor
public class AuthenticationController {
    
    private final AuthenticationService authenticationService;
    
    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Create a new user account")
    public ResponseEntity<ApiResponse<UserDTO>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        log.info("Register endpoint called for user: {}", registerRequest.getEmail());
        UserDTO userDTO = authenticationService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success(userDTO, "User registered successfully"));
    }
    
    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user and receive JWT token")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("Login endpoint called for user: {}", loginRequest.getUsername());
        AuthResponse authResponse = authenticationService.login(loginRequest);
        return ResponseEntity.ok(ApiResponse.success(authResponse, "Login successful"));
    }
    
    @PostMapping("/refresh-token")
    @Operation(summary = "Refresh JWT token", description = "Get a new JWT token using existing token")
    public ResponseEntity<ApiResponse<String>> refreshToken(@RequestBody String token) {
        log.info("Refresh token endpoint called");
        String newToken = authenticationService.refreshToken(token);
        return ResponseEntity.ok(ApiResponse.success(newToken, "Token refreshed successfully"));
    }
}
