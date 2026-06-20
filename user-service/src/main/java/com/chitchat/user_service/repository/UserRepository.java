package com.chitchat.user_service.repository;

import com.chitchat.user_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for {@link User} entities.
 *
 * <p>Extends {@link JpaRepository} to inherit standard CRUD operations and
 * pagination support. The {@code findByEmail} method is derived automatically
 * by Spring Data from the method name — no manual query needed.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Looks up a user by their email address.
     *
     * @param email the email address to search for (case-sensitive)
     * @return an {@link Optional} containing the matching {@link User},
     *         or {@link Optional#empty()} if no user exists with that email
     */
    Optional<User> findByEmail(String email);
}
