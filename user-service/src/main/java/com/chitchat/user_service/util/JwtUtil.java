package com.chitchat.user_service.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Utility component for generating JSON Web Tokens (JWTs).
 *
 * <p>
 * A cryptographically secure HS256 secret key is generated once when this
 * bean is initialised. The key lives only in memory for the duration of the
 * application process — tokens issued before a restart will be invalid after
 * the restart. For production use, externalise the key via an environment
 * variable or a secrets-management service.
 */
@Slf4j
@Component
public class JwtUtil {

    /** Token validity period — 24 hours expressed in milliseconds. */
    private static final long EXPIRATION_MS = 24L * 60 * 60 * 1000;

    /**
     * HS256 secret key generated at startup.
     * {@link Keys#secretKeyFor} guarantees a key that satisfies the minimum
     * length requirement for the chosen algorithm (256 bits for HS256).
     */
    private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    /**
     * Generates a signed JWT for the given email address.
     *
     * <p>
     * The token contains:
     * <ul>
     * <li><b>subject</b> — the user's email address</li>
     * <li><b>issuedAt</b> — the current UTC time</li>
     * <li><b>expiration</b> — 24 hours from issuance</li>
     * </ul>
     *
     * @param email the authenticated user's email, used as the JWT subject
     * @return a compact, URL-safe JWT string (header.payload.signature)
     */
    public String generateToken(String email) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + EXPIRATION_MS);

        String token = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(secretKey)
                .compact();

        log.debug("JWT generated for subject '{}', expires at {}", email, expiry);
        return token;
    }
}
