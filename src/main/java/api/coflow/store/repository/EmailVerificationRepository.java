package api.coflow.store.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import api.coflow.store.entity.EmailVerification;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, UUID> {
    
    void deleteAllByEmail(String email);

    Optional<EmailVerification> findByEmail(String email);
}
