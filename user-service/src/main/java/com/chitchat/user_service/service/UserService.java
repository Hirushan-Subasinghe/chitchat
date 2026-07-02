package com.chitchat.user_service.service;

import com.chitchat.user_service.dto.LoginRequest;
import com.chitchat.user_service.dto.RegisterRequest;
import com.chitchat.user_service.model.User;
import com.chitchat.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service layer for user-related business logic.
 *
 * <p>
 * All database-mutating operations are wrapped in a transaction via
 * {@link Transactional} to ensure atomicity and automatic rollback on failure.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Registers a new user from the given {@link RegisterRequest}.
     *
     * <p>
     * Steps performed:
     * <ol>
     * a
     * <li>Verify the email address is not already taken.</li>
     * <li>Map the DTO fields onto a new {@link User} entity.</li>
     * <li>Persist the entity and return the saved result.</li>
     * </ol>
     *
     *
     * @param request the registration payload from the client
     * @return the persisted {@link User} entity (with generated ID and createdAt)
     * @throws RuntimeException if the email address is already registered
     */
    @Transactional
    public User registerUser(RegisterRequest request) {
        log.info("Attempting to register user with email: {}", request.getEmail());

        // 1. Reject duplicate emails early to avoid a DB unique-constraint error
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            log.warn("Registration failed — email already in use: {}", request.getEmail());
            throw new RuntimeException("Email is already registered: " + request.getEmail());
        }

        // 2. Map DTO → Entity
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEnglishLevel(request.getEnglishLevel());
        // createdAt is set automatically by the @PrePersist hook in User

        // 3. Persist and return
        User saved = userRepository.save(user);
        log.info("User registered successfully with ID: {}", saved.getId());
        return saved;
    }

    /**
     * Authenticates a user using the given {@link LoginRequest}.
     *
     * <p>
     * Steps performed:
     * <ol>
     * <li>Locate the account by email address.</li>
     * <li>Verify the supplied plain-text password against the stored BCrypt
     * hash.</li>
     * <li>Return the authenticated {@link User} entity on success.</li>
     * </ol>
     *
     * <p>
     * Both "user not found" and "wrong password" throw the same generic exception
     * to prevent user-enumeration attacks.
     *
     * @param request the login payload from the client
     * @return the authenticated {@link User} entity
     * @throws RuntimeException if the email is not found or the password does not
     *                          match
     */
    public User loginUser(LoginRequest request) {
        log.info("Login attempt for email: {}", request.getEmail());

        // 1. Look up account — same exception as step 2 to prevent user enumeration
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.warn("Login failed — no account found for email: {}", request.getEmail());
                    return new RuntimeException("Invalid email or password");
                });

        // 2. Verify password against the stored BCrypt hash
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("Login failed — incorrect password for email: {}", request.getEmail());
            throw new RuntimeException("Invalid email or password");
        }

        // 3. Credentials valid — return the authenticated user
        log.info("Login successful for user ID: {}", user.getId());
        return user;
    }

    public java.util.List<User> getAllUsers() {
        log.info("Fetching all users");
        java.util.List<User> users = userRepository.findAll();
        log.info("Found {} user(s)", users.size());
        return users;
    }
}
