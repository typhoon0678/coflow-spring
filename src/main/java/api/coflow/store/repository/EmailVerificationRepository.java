package api.coflow.store.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import api.coflow.store.entity.EmailVerification;
import jakarta.transaction.Transactional;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, UUID> {

    void deleteAllByEmail(String email);

    Optional<EmailVerification> findByEmail(String email);

    @Modifying
    @Transactional
    @Query("UPDATE EmailVerification e SET e.verified = true WHERE e.email = :email")
    void updateVerifiedTrue(String email);
}
