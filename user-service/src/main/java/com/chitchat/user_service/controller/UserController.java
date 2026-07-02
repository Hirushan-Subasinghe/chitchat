package com.chitchat.user_service.controller;

import com.chitchat.user_service.dto.AuthResponse;
import com.chitchat.user_service.dto.LoginRequest;
import com.chitchat.user_service.dto.RegisterRequest;
import com.chitchat.user_service.model.User;
import com.chitchat.user_service.service.UserService;
import com.chitchat.user_service.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller exposing user-related endpoints.
 *
 * <p>Base path: {@code /api/users}
 */
@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    /**
     * Registers a new user account.
     *
     * <p>The request body is validated before reaching the service layer.
     * If any constraint fails (e.g. blank email, short password), Spring returns
     * a {@code 400 Bad Request} automatically.
     *
     * @param request the validated registration payload
     * @return {@code 201 Created} with the persisted {@link User} in the body
     */
    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody RegisterRequest request) {
        log.info("POST /api/users/register — email: {}", request.getEmail());

        User savedUser = userService.registerUser(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedUser);
    }

    /**
     * Authenticates an existing user and returns a signed JWT.
     *
     * <p>The request body is validated before reaching the service layer.
     * If any constraint fails (e.g. blank email), Spring returns
     * a {@code 400 Bad Request} automatically.
     *
     * @param request the validated login payload
     * @return {@code 200 OK} with an {@link AuthResponse} containing the JWT
     *         and the authenticated {@link User}'s details
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("POST /api/users/login — email: {}", request.getEmail());

        User authenticatedUser = userService.loginUser(request);
        String token = jwtUtil.generateToken(authenticatedUser.getEmail());

        AuthResponse response = AuthResponse.builder()
                .token(token)
                .user(authenticatedUser)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @org.springframework.web.bind.annotation.GetMapping
    public ResponseEntity<java.util.List<User>> getAllUsers() {
        log.info("GET /api/users");
        java.util.List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}

