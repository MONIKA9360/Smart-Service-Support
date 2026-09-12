package com.example.support.dto.request;

import com.example.support.entity.enums.TicketPriority;
import com.example.support.entity.enums.TicketStatus;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Request payload for updating an existing support ticket's status, priority, or assignment.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketUpdateRequest {

    private Long assignedEmployeeId;

    private Long serviceId;

    @Size(max = 200, message = "Title must not exceed 200 characters")
    private String title;

    private String description;

    private TicketPriority priority;

    private TicketStatus status;

    private String resolution;
}
