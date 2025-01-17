package api.coflow.store.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import api.coflow.store.entity.Refresh;
import jakarta.transaction.Transactional;

public interface RefreshRepository extends JpaRepository<Refresh, UUID> {

    @Transactional
    void deleteAllByEmail(String email);

    @Query("SELECT COUNT(r) FROM Refresh r WHERE r.jwt = :refreshToken")
    int isExistsRefreshToken(String refreshToken);
}
