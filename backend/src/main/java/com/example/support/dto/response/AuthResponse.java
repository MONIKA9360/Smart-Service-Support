package com.example.support.dto.response;

import lombok.*;

/**
 * Authentication response containing JWT access token and sanitized user profile.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {

    private String accessToken;

    @Builder.Default
    private String tokenType = "Bearer";

    private Long expiresIn;

    private UserResponse user;
}
