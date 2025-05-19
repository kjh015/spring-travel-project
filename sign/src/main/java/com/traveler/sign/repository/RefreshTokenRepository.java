package com.traveler.sign.repository;

import com.traveler.sign.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    void deleteByLoginId(String loginId);
    Optional<RefreshToken> findByLoginId(String loginId);

}
