package com.example.travelplanner.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.HexFormat;

import org.springframework.stereotype.Service;

import com.example.travelplanner.entity.EmailVerificationToken;
import com.example.travelplanner.entity.User;
import com.example.travelplanner.repository.EmailVerificationTokenRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmailVerificationService {

    private final EmailVerificationTokenRepository tokenRepository;

    private final SecureRandom secureRandom = new SecureRandom();

    public EmailVerificationService(
            EmailVerificationTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public String generateVerificationToken(User user) {

        // Generate 32 cryptographically secure random bytes.
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);

        // Convert bytes into a URL-safe token.
        String rawToken = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(randomBytes);

        // Store only the hash, not the original token.
        String tokenHash = hashToken(rawToken);

        EmailVerificationToken verificationToken =
                new EmailVerificationToken();

        verificationToken.setUser(user);
        verificationToken.setTokenHash(tokenHash);
        verificationToken.setExpiresAt(
                Instant.now().plus(24, ChronoUnit.HOURS)
        );
        verificationToken.setUsed(false);

        tokenRepository.save(verificationToken);

        // The raw token will later be included in the verification email.
        return rawToken;
    }

    @Transactional
public void verifyEmail(String rawToken) {

    if (rawToken == null || rawToken.isBlank()) {
        throw new IllegalArgumentException(
                "Verification token is required"
        );
    }

    String tokenHash = hashToken(rawToken);

    EmailVerificationToken verificationToken = tokenRepository
        .findByTokenHashForUpdate(tokenHash)
        .orElseThrow(() -> new IllegalArgumentException(
                "Invalid verification token"
        ));

    if (verificationToken.isUsed()) {
        throw new IllegalArgumentException(
                "This verification link has already been used"
        );
    }

    if (!Instant.now().isBefore(verificationToken.getExpiresAt())) {
        throw new IllegalArgumentException(
                "This verification link has expired"
        );
    }

    User user = verificationToken.getUser();

    user.setEmailVerified(true);
    verificationToken.setUsed(true);

    // Both changes are persisted when the transaction commits.
}

    private String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(
                    token.getBytes(StandardCharsets.UTF_8)
            );

            return HexFormat.of().formatHex(hash);

        } catch (java.security.NoSuchAlgorithmException e) {
            throw new IllegalStateException(
                    "SHA-256 algorithm is unavailable", e
            );
        }
    }
}