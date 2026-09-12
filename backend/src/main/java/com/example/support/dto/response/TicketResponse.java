package com.example.support.dto.response;

import com.example.support.entity.enums.TicketPriority;
import com.example.support.entity.enums.TicketStatus;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Response DTO for ticket details and dashboard cards.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketResponse {

    private Long id;
    private String ticketNumber;
    private Long customerId;
    private String customerName;
    private String customerCode;
    private Long assignedEmployeeId;
    private String assignedEmployeeName;
    private Long serviceId;
    private String serviceName;
    private String title;
    private String description;
    private TicketPriority priority;
    private TicketStatus status;
    private String resolution;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime assignedAt;
    private LocalDateTime resolvedAt;
    private LocalDateTime closedAt;
}
