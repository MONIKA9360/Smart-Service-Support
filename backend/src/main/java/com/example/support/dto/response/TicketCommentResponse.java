package com.example.support.dto.response;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Response DTO for ticket comments.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketCommentResponse {

    private Long id;
    private Long ticketId;
    private Long userId;
    private String userName;
    private String userRole;
    private String commentText;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
