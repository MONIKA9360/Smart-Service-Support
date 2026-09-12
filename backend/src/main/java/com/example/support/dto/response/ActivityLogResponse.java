package com.example.support.dto.response;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Response DTO for system audit and activity log entries.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityLogResponse {

    private Long id;
    private Long userId;
    private String userName;
    private String action;
    private String entityType;
    private Long entityId;
    private String description;
    private LocalDateTime createdAt;
}
