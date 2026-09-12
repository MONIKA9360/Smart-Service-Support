package com.example.support.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Phase 0 Security Configuration.
 *
 * NOTE: This is a SKELETON configuration for Phase 0 scaffold only.
 *       JWT filter, UserDetailsService, and full role-based access control
 *       will be implemented in Phase 2 (Authentication & Security phase).
 *
 * Currently permits all requests so the health endpoint and Swagger UI
 * are accessible for verification purposes.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    /**
     * BCrypt password encoder — will be used by AuthService in Phase 2.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    /**
     * Phase 0: Permit all endpoints for scaffold verification.
     * Phase 2: This will be replaced with JWT-based stateless security.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Public endpoints (always accessible)
                .requestMatchers(
                    "/actuator/**",
                    "/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/v3/api-docs/**"
                ).permitAll()
                // Phase 0: Permit everything — will be locked down in Phase 2
                .anyRequest().permitAll()
            );

        return http.build();
    }
}
