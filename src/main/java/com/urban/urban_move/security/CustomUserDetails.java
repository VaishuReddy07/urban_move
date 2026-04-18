package com.urban.urban_move.security;

import com.urban.urban_move.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Custom UserDetails implementation for Spring Security.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomUserDetails implements UserDetails {
    
    private Long id;
    private String username;
    private String email;
    private String password;
    private String fullName;
    private Boolean accountLocked;
    private Boolean isActive;
    private Collection<? extends GrantedAuthority> authorities;
    
    public static CustomUserDetails build(User user) {
        List<GrantedAuthority> authorities = Collections.singletonList(
            new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
        );
        
        return CustomUserDetails.builder()
            .id(user.getId())
            .username(user.getUsername())
            .email(user.getEmail())
            .password(user.getPassword())
            .fullName(user.getFullName())
            .accountLocked(user.getAccountLocked())
            .isActive(user.getIsActive())
            .authorities(authorities)
            .build();
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    
    @Override
    public String getPassword() {
        return password;
    }
    
    @Override
    public String getUsername() {
        return username;
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return isActive;
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return !accountLocked;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return isActive;
    }
    
    @Override
    public boolean isEnabled() {
        return isActive;
    }
}
