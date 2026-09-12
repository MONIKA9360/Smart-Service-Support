package com.example.support.controller;

import com.example.support.dto.request.LoginRequest;
import com.example.support.dto.request.RegisterRequest;
import com.example.support.dto.response.ApiResponse;
import com.example.support.dto.response.AuthResponse;
import com.example.support.dto.response.UserResponse;
import com.example.support.security.UserPrincipal;
import com.example.support.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for customer registration, user authentication, and profile discovery.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for customer registration, login, and current session profile")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register a new customer account", description = "Public endpoint. Always assigns ROLE_CUSTOMER and generates a Customer profile.")
    public ResponseEntity<ApiResponse<UserResponse>> register(@Valid @RequestBody RegisterRequest request) {
        UserResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Account registered successfully", response));
    }

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticates credentials and returns a short-lived JWT access token.")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Login successful", response));
    }

    @GetMapping("/me")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get current authenticated user profile", description = "Requires a valid JWT Bearer token.")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser(@AuthenticationPrincipal UserPrincipal principal) {
        UserResponse response = authService.getCurrentUser(principal);
        return ResponseEntity.ok(ApiResponse.success("User profile retrieved successfully", response));
    }
}
