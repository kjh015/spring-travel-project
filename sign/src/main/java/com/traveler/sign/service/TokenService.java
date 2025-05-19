package com.traveler.sign.service;

import com.traveler.sign.entity.RefreshToken;
import com.traveler.sign.repository.RefreshTokenRepository;
import com.traveler.sign.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;



    public String reissueAccessToken(String refreshToken) {
        // 토큰 자체가 유효한지 검사
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new InvalidTokenException("유효하지 않은 refreshToken");
        }

        // 토큰에서 사용자 ID 추출
        String loginId = jwtTokenProvider.getUsername(refreshToken);

        // DB에 저장된 refreshToken과 비교
        RefreshToken saved = refreshTokenRepository.findByLoginId(loginId)
                .orElseThrow(() -> new InvalidTokenException("저장된 refreshToken 없음"));

        if (!saved.getRefreshToken().equals(refreshToken)) {
            throw new InvalidTokenException("refreshToken이 일치하지 않음");
        }

        // 토큰에서 Role 추출
        List<String> roles = jwtTokenProvider.getRoles(refreshToken);

        // 새로운 accessToken 발급
        return jwtTokenProvider.createAccessToken(loginId, roles);
    }



}
