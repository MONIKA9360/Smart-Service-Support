package com.example.support.controller;

import com.example.support.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Controller providing role-protected baseline endpoints for RBAC verification.
 */
@RestController
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "RBAC Verification", description = "Role-based authorization boundary verification endpoints")
public class RbacCheckController {

    @GetMapping("/admin/check")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Admin RBAC boundary check", description = "Accessible only by users with ROLE_ADMIN.")
    public ResponseEntity<ApiResponse<Map<String, String>>> checkAdmin() {
        return ResponseEntity.ok(ApiResponse.success("Admin access authorized", Map.of("role", "ROLE_ADMIN")));
    }

    @GetMapping("/agent/check")
    @PreAuthorize("hasRole('AGENT')")
    @Operation(summary = "Agent RBAC boundary check", description = "Accessible only by users with ROLE_AGENT.")
    public ResponseEntity<ApiResponse<Map<String, String>>> checkAgent() {
        return ResponseEntity.ok(ApiResponse.success("Agent access authorized", Map.of("role", "ROLE_AGENT")));
    }

    @GetMapping("/customer/check")
    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "Customer RBAC boundary check", description = "Accessible only by users with ROLE_CUSTOMER.")
    public ResponseEntity<ApiResponse<Map<String, String>>> checkCustomer() {
        return ResponseEntity.ok(ApiResponse.success("Customer access authorized", Map.of("role", "ROLE_CUSTOMER")));
    }
}
