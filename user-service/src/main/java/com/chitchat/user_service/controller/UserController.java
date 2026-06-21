package com.chitchat.user_service.controller;

import com.chitchat.user_service.dto.LoginRequest;
import com.chitchat.user_service.dto.RegisterRequest;
import com.chitchat.user_service.model.User;
import com.chitchat.user_service.service.UserService;
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
     * Authenticates an existing user.
     *
     * <p>The request body is validated before reaching the service layer.
     * If any constraint fails (e.g. blank email), Spring returns
     * a {@code 400 Bad Request} automatically.
     *
     * @param request the validated login payload
     * @return {@code 200 OK} with the authenticated {@link User} in the body
     */
    @PostMapping("/login")
    public ResponseEntity<User> login(@Valid @RequestBody LoginRequest request) {
        log.info("POST /api/users/login — email: {}", request.getEmail());

        User authenticatedUser = userService.loginUser(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authenticatedUser);
    }
}
