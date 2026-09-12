package com.example.support.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler.
 * Returns consistent JSON error responses across all controllers.
 *
 * NOTE: Additional exception handlers (ResourceNotFound, AccessDenied, etc.)
 *       will be added in Phase 2 and later phases.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ── Validation Errors ────────────────────────────────────────────────────
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(
            MethodArgumentNotValidException ex) {

        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
          .forEach(err -> fieldErrors.put(err.getField(), err.getDefaultMessage()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(errorResponse("Validation failed", "VALIDATION_ERROR", fieldErrors));
    }

    // ── Resource Not Found ───────────────────────────────────────────────────
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFound(
            ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(errorResponse(ex.getMessage(), "RESOURCE_NOT_FOUND", null));
    }

    // ── Generic Fallback ─────────────────────────────────────────────────────
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(errorResponse("An unexpected error occurred", "INTERNAL_ERROR", null));
    }

    // ── Helper ───────────────────────────────────────────────────────────────
    private Map<String, Object> errorResponse(String message, String errorCode, Object details) {
        Map<String, Object> body = new HashMap<>();
        body.put("success", false);
        body.put("message", message);
        body.put("errorCode", errorCode);
        if (details != null) {
            body.put("details", details);
        }
        return body;
    }
}
