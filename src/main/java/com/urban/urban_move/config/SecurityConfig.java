package com.urban.urban_move.config;

import com.urban.urban_move.security.CustomUserDetailsService;
import com.urban.urban_move.security.JwtAuthenticationFilter;
import com.urban.urban_move.security.JwtAuthenticationEntryPoint;
import com.urban.urban_move.security.CustomAuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security Configuration for OAuth2 JWT authentication.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
    securedEnabled = true,
    jsr250Enabled = true,
    prePostEnabled = true
)
@RequiredArgsConstructor
public class SecurityConfig {
    
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAuthenticationSuccessHandler authenticationSuccessHandler;
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.disable())
            .csrf(csrf -> csrf.disable())
            .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authz -> authz
                // Public pages - Homepage, Login, Register
                .requestMatchers("/", "/login", "/register", "/error").permitAll()
                
                // Static resources and webjars
                .requestMatchers("/static/**", "/css/**", "/js/**", "/images/**", "/fonts/**", "/webjars/**").permitAll()
                .requestMatchers("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.gif", "/**/*.ico", "/**/*.svg", "/**/*.woff", "/**/*.woff2").permitAll()
                
                // Public API endpoints
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/public/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                .requestMatchers("/actuator/health").permitAll()
                
                // Protected web pages - require authentication
                .requestMatchers("/dashboard", "/vehicles", "/routes", "/analytics", "/profile", "/settings").authenticated()
                
                // Admin endpoints
                .requestMatchers(HttpMethod.POST, "/api/admin/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/admin/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/admin/**").hasRole("ADMIN")
                
                // User endpoints
                .requestMatchers("/api/users/**").hasAnyRole("ADMIN", "OPERATOR", "DRIVER", "RIDER")
                
                // Vehicle endpoints
                .requestMatchers(HttpMethod.GET, "/api/vehicles/**").hasAnyRole("ADMIN", "OPERATOR", "DRIVER", "RIDER")
                .requestMatchers(HttpMethod.POST, "/api/vehicles").hasAnyRole("ADMIN", "OPERATOR")
                .requestMatchers(HttpMethod.PUT, "/api/vehicles/**").hasAnyRole("ADMIN", "OPERATOR", "DRIVER")
                
                // Route endpoints
                .requestMatchers(HttpMethod.GET, "/api/routes/**").hasAnyRole("ADMIN", "OPERATOR", "DRIVER", "RIDER")
                .requestMatchers(HttpMethod.POST, "/api/routes").hasAnyRole("ADMIN", "OPERATOR")
                .requestMatchers(HttpMethod.PUT, "/api/routes/**").hasAnyRole("ADMIN", "OPERATOR", "DRIVER")
                
                // Fleet endpoints
                .requestMatchers(HttpMethod.GET, "/api/fleets/**").hasAnyRole("ADMIN", "OPERATOR")
                .requestMatchers(HttpMethod.POST, "/api/fleets").hasRole("ADMIN")
                
                // All other requests require authentication
                .anyRequest().authenticated()
            )
            .formLogin(formLogin -> formLogin
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .successHandler(authenticationSuccessHandler)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .permitAll()
            );
        
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
}
