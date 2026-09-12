package com.example.support.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Phase 0 health check controller.
 * Provides a simple status endpoint so the scaffold can be verified
 * independently from the Actuator endpoint.
 *
 * NOTE: This controller will be removed or merged after Phase 2.
 */
@RestController
@RequestMapping("/health-check")
@Tag(name = "Health", description = "Application health check endpoints")
public class HealthController {

    @GetMapping
    @Operation(summary = "Application health check", description = "Returns the current status of the application")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
            "success", true,
            "status",  "UP",
            "message", "Smart Service & Support Management System is running",
            "timestamp", LocalDateTime.now().toString(),
            "version", "0.0.1-SNAPSHOT (Phase 0 — Scaffold)"
        ));
    }
}
