package com.urban.urban_move.service;

import com.urban.urban_move.domain.User;
import com.urban.urban_move.dto.UserDTO;
import com.urban.urban_move.exception.ResourceNotFoundException;
import com.urban.urban_move.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * User service for user management operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    /**
     * Get user by ID.
     */
    @Transactional(readOnly = true)
    public UserDTO getUserById(Long id) {
        log.info("Fetching user with ID: {}", id);
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return convertToDTO(user);
    }
    
    /**
     * Get user by username.
     */
    @Transactional(readOnly = true)
    public UserDTO getUserByUsername(String username) {
        log.info("Fetching user with username: {}", username);
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        return convertToDTO(user);
    }
    
    /**
     * Get all users with pagination.
     */
    @Transactional(readOnly = true)
    public Page<UserDTO> getAllUsers(Pageable pageable) {
        log.info("Fetching all users with pagination");
        return userRepository.findAll(pageable)
            .map(this::convertToDTO);
    }
    
    /**
     * Update user profile.
     */
    @Transactional
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        log.info("Updating user with ID: {}", id);
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        
        if (userDTO.getFullName() != null) {
            user.setFullName(userDTO.getFullName());
        }
        if (userDTO.getPhoneNumber() != null) {
            user.setPhoneNumber(userDTO.getPhoneNumber());
        }
        
        User updatedUser = userRepository.save(user);
        log.info("User updated successfully: {}", id);
        return convertToDTO(updatedUser);
    }
    
    /**
     * Change user password.
     */
    @Transactional
    public void changePassword(Long id, String oldPassword, String newPassword) {
        log.info("Changing password for user ID: {}", id);
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        log.info("Password changed successfully for user: {}", id);
    }
    
    /**
     * Suspend user account.
     */
    @Transactional
    public void suspendUser(Long id) {
        log.info("Suspending user with ID: {}", id);
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        user.setStatus(User.UserStatus.SUSPENDED);
        user.setAccountLocked(true);
        userRepository.save(user);
    }
    
    /**
     * Activate user account.
     */
    @Transactional
    public void activateUser(Long id) {
        log.info("Activating user with ID: {}", id);
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        user.setStatus(User.UserStatus.ACTIVE);
        user.setAccountLocked(false);
        user.setLoginAttempts(0L);
        userRepository.save(user);
    }
    
    /**
     * Verify user email.
     */
    @Transactional
    public void verifyEmail(Long id) {
        log.info("Verifying email for user ID: {}", id);
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        user.setEmailVerified(true);
        userRepository.save(user);
    }
    
    /**
     * Convert User entity to UserDTO.
     */
    public UserDTO convertToDTO(User user) {
        return UserDTO.builder()
            .id(user.getId())
            .username(user.getUsername())
            .email(user.getEmail())
            .fullName(user.getFullName())
            .phoneNumber(user.getPhoneNumber())
            .userRole(user.getRole().name())
            .emailVerified(user.getEmailVerified())
            .accountLocked(user.getAccountLocked())
            .status(user.getStatus().name())
            .createdAt(user.getCreatedAt())
            .updatedAt(user.getUpdatedAt())
            .build();
    }
}
