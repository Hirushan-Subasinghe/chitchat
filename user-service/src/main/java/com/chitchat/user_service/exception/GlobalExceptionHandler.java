package com.chitchat.user_service.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Global exception handler for the user-service.
 *
 * <p>Intercepts exceptions thrown from any {@code @RestController} and maps
 * them to structured JSON error responses, preventing raw stack traces from
 * leaking to clients.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles {@link RuntimeException} thrown during request processing (e.g.
     * invalid credentials, resource not found).
     *
     * <p>Returns a {@code 401 Unauthorized} response with a structured error
     * body so that clients receive a consistent, machine-readable error format.
     *
     * @param ex the runtime exception that was thrown
     * @return {@code 401 Unauthorized} with error details in the body
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        log.warn("RuntimeException intercepted: {}", ex.getMessage());

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("status", HttpStatus.UNAUTHORIZED.value());
        body.put("error", "Unauthorized");
        body.put("message", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(body);
    }
}
