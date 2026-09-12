package com.example.support.dto.response;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Public user profile response DTO — strictly excludes passwordHash.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Long roleId;
    private String roleName;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
