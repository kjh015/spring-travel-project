package com.traveler.sign.repository;

import com.traveler.sign.entity.RefreshToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    void deleteByLoginId(String loginId);

    Optional<RefreshToken> findByLoginId(String loginId);
}
