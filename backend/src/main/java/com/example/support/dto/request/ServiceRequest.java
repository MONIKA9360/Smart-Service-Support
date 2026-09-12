package com.example.support.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Request payload for creating or updating a support service catalog entry.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceRequest {

    @NotBlank(message = "Service name is required")
    @Size(max = 100, message = "Service name must not exceed 100 characters")
    private String serviceName;

    private String description;

    @Size(max = 100, message = "Category must not exceed 100 characters")
    private String category;

    @Builder.Default
    private Boolean isActive = true;
}
