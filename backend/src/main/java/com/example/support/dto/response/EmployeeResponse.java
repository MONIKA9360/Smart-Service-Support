package com.example.support.dto.response;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Response DTO for employee/agent profile data.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeResponse {

    private Long id;
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String employeeCode;
    private String department;
    private String designation;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
