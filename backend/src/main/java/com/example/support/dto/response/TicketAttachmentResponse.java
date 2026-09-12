package com.example.support.dto.response;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Response DTO for ticket attachment metadata.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketAttachmentResponse {

    private Long id;
    private Long ticketId;
    private Long uploadedById;
    private String uploadedByName;
    private String originalFileName;
    private String storedFileName;
    private String filePath;
    private String fileType;
    private Long fileSize;
    private LocalDateTime createdAt;
}
