package com.urban.urban_move.controller;

import com.urban.urban_move.dto.ApiResponse;
import com.urban.urban_move.dto.UserDTO;
import com.urban.urban_move.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * User management REST API controller.
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Users", description = "User management endpoints")
@RequiredArgsConstructor
@Slf4j
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {
    
    private final UserService userService;
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR', 'DRIVER', 'RIDER')")
    @Operation(summary = "Get user by ID", description = "Retrieve user information by ID")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(@PathVariable Long id) {
        log.info("Get user endpoint called with ID: {}", id);
        UserDTO userDTO = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success(userDTO));
    }
    
    @GetMapping("/username/{username}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    @Operation(summary = "Get user by username", description = "Retrieve user information by username")
    public ResponseEntity<ApiResponse<UserDTO>> getUserByUsername(@PathVariable String username) {
        log.info("Get user by username endpoint called with: {}", username);
        UserDTO userDTO = userService.getUserByUsername(username);
        return ResponseEntity.ok(ApiResponse.success(userDTO));
    }
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all users", description = "Retrieve all users with pagination")
    public ResponseEntity<ApiResponse<Page<UserDTO>>> getAllUsers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size) {
        log.info("Get all users endpoint called with page: {}, size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<UserDTO> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(ApiResponse.success(users));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RIDER', 'DRIVER', 'OPERATOR') and (hasRole('ADMIN') or @authUtils.isCurrentUser(#id))")
    @Operation(summary = "Update user profile", description = "Update user information")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(
        @PathVariable Long id,
        @RequestBody UserDTO userDTO) {
        log.info("Update user endpoint called with ID: {}", id);
        UserDTO updatedUser = userService.updateUser(id, userDTO);
        return ResponseEntity.ok(ApiResponse.success(updatedUser, "User updated successfully"));
    }
}
