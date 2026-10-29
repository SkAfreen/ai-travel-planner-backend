package com.example.travelplanner.repository;

import com.example.travelplanner.entity.EmailVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmailVerificationTokenRepository
        extends JpaRepository<EmailVerificationToken, Long> {

    Optional<EmailVerificationToken> findByTokenHash(String tokenHash);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
@Query("""
       SELECT t
       FROM EmailVerificationToken t
       WHERE t.tokenHash = :tokenHash
       """)
Optional<EmailVerificationToken> findByTokenHashForUpdate(
        @Param("tokenHash") String tokenHash
);
}