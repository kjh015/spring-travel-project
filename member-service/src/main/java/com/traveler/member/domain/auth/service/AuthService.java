package com.traveler.member.domain.auth.service;

import com.traveler.member.domain.auth.dto.AuthTokens;
import com.traveler.member.domain.auth.dto.request.AuthRequest;
import com.traveler.member.domain.auth.dto.response.AuthResponse;
import com.traveler.member.domain.auth.mapper.AuthMapper;
import com.traveler.member.domain.auth.repository.RefreshTokenRepository;
import com.traveler.member.domain.auth.repository.TokenBlacklistRepository;
import com.traveler.member.domain.auth.support.JwtTokenProvider;
import com.traveler.member.domain.member.entity.Member;
import com.traveler.member.domain.member.enums.RoleType;
import com.traveler.member.domain.member.repository.MemberRepository;
import com.traveler.member.global.exception.MemberServiceException;
import com.traveler.member.global.exception.code.MemberServiceErrorCode;
import io.jsonwebtoken.Claims;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenBlacklistRepository tokenBlacklistRepository;
    private final AuthMapper authMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse.LoginResult login(AuthRequest.LoginDTO dto) {
        Member member = memberRepository
                .findByLoginIdWithRoles(dto.loginId())
                .orElseThrow(() -> new MemberServiceException(MemberServiceErrorCode.MEMBER_NOT_FOUND));

        validatePassword(dto.password(), member.getPassword());

        AuthTokens tokens = createAuthTokens(member);

        refreshTokenRepository.save(
                member.getId(), tokens.refreshToken(), jwtTokenProvider.getRefreshTokenExpireTime());

        return authMapper.toLoginResultDTO(tokens, member);
    }

    public void logout(Long memberId, String accessToken) {
        // Redis에 저장된 해당 유저의 Refresh Token 삭제
        refreshTokenRepository.deleteByMemberId(memberId);

        long remainingTime = jwtTokenProvider.getRemainingExpirationTime(accessToken);
        if (remainingTime > 0) {
            tokenBlacklistRepository.save(accessToken, remainingTime);
        }
    }

    public AuthResponse.LoginResult reissue(String refreshToken) {
        // Refresh Token 유효성 검증
        Claims claims = jwtTokenProvider.validateToken(refreshToken);
        String loginId = jwtTokenProvider.getUserId(claims);

        // 사용자 및 저장된 토큰 확인
        Member member = memberRepository
                .findByLoginIdWithRoles(loginId)
                .orElseThrow(() -> new MemberServiceException(MemberServiceErrorCode.MEMBER_NOT_FOUND));

        validateStoredRefreshToken(member.getId(), refreshToken);

        // 새로운 토큰 생성 (Rotation)
        AuthTokens tokens = createAuthTokens(member);

        // Redis 갱신
        refreshTokenRepository.save(
                member.getId(), tokens.refreshToken(), jwtTokenProvider.getRefreshTokenExpireTime());

        return authMapper.toLoginResultDTO(tokens, member);
    }

    private AuthTokens createAuthTokens(Member member) {
        List<RoleType> roles = member.getRoleTypes();
        String at = jwtTokenProvider.createAccessToken(member.getId(), roles);
        String rt = jwtTokenProvider.createRefreshToken(member.getId(), roles);
        return new AuthTokens(at, rt);
    }

    private void validatePassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new MemberServiceException(MemberServiceErrorCode.INVALID_PASSWORD);
        }
    }

    private void validateStoredRefreshToken(Long memberId, String requestToken) {
        String savedToken = refreshTokenRepository
                .findByMemberId(memberId)
                .orElseThrow(() -> new MemberServiceException(MemberServiceErrorCode.INVALID_TOKEN_TYPE));

        if (!savedToken.equals(requestToken)) {
            refreshTokenRepository.deleteByMemberId(memberId);
            throw new MemberServiceException(MemberServiceErrorCode.INVALID_TOKEN_TYPE);
        }
    }
}
