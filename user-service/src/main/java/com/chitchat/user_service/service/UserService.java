package com.chitchat.user_service.service;

import com.chitchat.user_service.dto.RegisterRequest;
import com.chitchat.user_service.model.User;
import com.chitchat.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
     * <p>
     * <strong>Note:</strong> Password is stored as plain text for now.
     * Replace with a hashing mechanism (e.g., BCrypt) before going to production.
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
        // TODO: replace plain-text password with BCrypt hashing
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setEnglishLevel(request.getEnglishLevel());
        // createdAt is set automatically by the @PrePersist hook in User

        // 3. Persist and return
        User saved = userRepository.save(user);
        log.info("User registered successfully with ID: {}", saved.getId());
        return saved;
    }
}
