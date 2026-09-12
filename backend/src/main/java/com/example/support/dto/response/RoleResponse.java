package com.example.support.dto.response;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Response DTO for security roles.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleResponse {

    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
