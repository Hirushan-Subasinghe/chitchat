package com.chitchat.user_service.dto;

import com.chitchat.user_service.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO returned after a successful authentication.
 *
 * <p>Bundles the signed JWT and the authenticated user's details into a
 * single payload so clients receive everything they need in one response.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    /** Signed JWT the client should attach to subsequent requests. */
    private String token;

    /** The authenticated user's full profile. */
    private User user;
}
