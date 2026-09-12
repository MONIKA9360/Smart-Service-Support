package com.example.support.dto.response;

import com.example.support.entity.enums.TicketStatus;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Response DTO for ticket status transition audit records.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketStatusHistoryResponse {

    private Long id;
    private Long ticketId;
    private TicketStatus oldStatus;
    private TicketStatus newStatus;
    private Long changedById;
    private String changedByName;
    private String comment;
    private LocalDateTime createdAt;
}
