package com.chitchat.user_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) carrying the payload for a new user registration request.
 *
 * <p>Validation is enforced via Jakarta Bean Validation annotations. Controllers
 * should annotate the corresponding method parameter with {@code @Valid} to
 * trigger constraint checks before the request reaches the service layer.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    /**
     * The display name of the user.
     * Must not be blank.
     */
    @NotBlank(message = "Name is required")
    private String name;

    /**
     * The user's email address.
     * Must not be blank and must be a well-formed email.
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be a valid email address")
    private String email;

    /**
     * The user's chosen password (plain-text; must be hashed before persistence).
     * Must not be blank and must be at least 6 characters long.
     */
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    /**
     * The user's self-reported English proficiency level
     * (e.g., "Beginner", "Intermediate", "Advanced").
     */
    private String englishLevel;
}
