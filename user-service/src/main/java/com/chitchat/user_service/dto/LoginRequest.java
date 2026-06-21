package com.chitchat.user_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) carrying the payload for a user login request.
 *
 * <p>Validation is enforced via Jakarta Bean Validation annotations. Controllers
 * should annotate the corresponding method parameter with {@code @Valid} to
 * trigger constraint checks before the request reaches the service layer.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    /**
     * The user's email address.
     * Must not be blank and must be a well-formed email.
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be a valid email address")
    private String email;

    /**
     * The user's password (plain-text; matched against the stored hash).
     * Must not be blank.
     */
    @NotBlank(message = "Password is required")
    private String password;
}
