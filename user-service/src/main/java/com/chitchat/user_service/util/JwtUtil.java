package com.chitchat.user_service.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Utility component for generating JSON Web Tokens (JWTs).
 *
 * <p>
 * The HS256 secret key is decoded from a Base64-encoded string supplied via
 * the {@code jwt.secret} property (backed by the {@code JWT_SECRET} environment
 * variable). Using an externalised, persistent key means tokens remain valid
 * across application restarts.
 */
@Slf4j
@Component
public class JwtUtil {

    /** Token validity period — 24 hours expressed in milliseconds. */
    private static final long EXPIRATION_MS = 24L * 60 * 60 * 1000;

    /**
     * HS256 secret key decoded from the Base64 string in {@code jwt.secret}.
     * {@link Keys#hmacShaKeyFor} validates that the decoded byte array meets
     * the minimum length requirement for HMAC-SHA256 (256 bits / 32 bytes).
     */
    private final SecretKey secretKey;

    /**
     * Constructs the utility bean and initialises the signing key.
     *
     * @param jwtSecretBase64 Base64-encoded secret injected from
     *                        {@code application.properties} / environment
     */
    public JwtUtil(@Value("${jwt.secret}") String jwtSecretBase64) {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecretBase64);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        log.info("JwtUtil initialised — signing key loaded ({} bytes)", keyBytes.length);
    }

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
                .subject(email)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(secretKey)
                .compact();

        log.debug("JWT generated for subject '{}', expires at {}", email, expiry);
        return token;
    }
}
