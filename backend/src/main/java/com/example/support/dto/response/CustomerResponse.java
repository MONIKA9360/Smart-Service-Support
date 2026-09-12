package com.example.support.dto.response;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Response DTO for customer profile data.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerResponse {

    private Long id;
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String customerCode;
    private String address;
    private String city;
    private String state;
    private String postalCode;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
