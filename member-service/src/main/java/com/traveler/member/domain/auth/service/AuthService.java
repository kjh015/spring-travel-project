package com.traveler.member.domain.auth.service;

import com.traveler.common.core.auth.AuthConstants;
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
import com.traveler.member.global.util.TokenHashUtil;
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
                .findActiveByLoginIdWithRoles(dto.loginId())
                .orElseThrow(() -> new MemberServiceException(MemberServiceErrorCode.MEMBER_NOT_FOUND));

        validatePassword(dto.password(), member.getPassword());

        AuthTokens tokens = createAuthTokens(member);

        saveRefreshToken(member.getId(), tokens.refreshToken());

        return authMapper.toLoginResultDTO(tokens, member);
    }

    public void logout(Long memberId, String accessToken) {
        // Redis에 저장된 해당 유저의 Refresh Token 삭제
        refreshTokenRepository.deleteByMemberId(memberId);

        long remainingTime = jwtTokenProvider.getRemainingExpirationTime(accessToken);
        if (remainingTime > 0) {
            tokenBlacklistRepository.save(TokenHashUtil.hash(accessToken), remainingTime);
        }
    }

    public AuthResponse.LoginResult reissue(String refreshToken) {
        // Refresh Token 유효성 검증
        Claims claims = jwtTokenProvider.validateToken(refreshToken);

        String tokenType = jwtTokenProvider.getTokenType(claims);
        if (!AuthConstants.TOKEN_TYPE_REFRESH.equals(tokenType)) {
            throw new MemberServiceException(MemberServiceErrorCode.INVALID_TOKEN_TYPE);
        }

        Long userId = jwtTokenProvider.getUserId(claims);

        // 사용자 및 저장된 토큰 확인
        Member member = memberRepository
                .findByIdWithRoles(userId)
                .orElseThrow(() -> new MemberServiceException(MemberServiceErrorCode.MEMBER_NOT_FOUND));

        validateStoredRefreshToken(member.getId(), refreshToken);

        // 새로운 토큰 생성 (Rotation)
        AuthTokens tokens = createAuthTokens(member);

        // Redis 갱신
        saveRefreshToken(member.getId(), tokens.refreshToken());

        return authMapper.toLoginResultDTO(tokens, member);
    }

    public AuthResponse.LoginResult loginOrSignup(AuthRequest.OAuthLoginDTO dto) {

        Member member = memberRepository
                .findByProviderAndProviderIdAndIsDeletedFalse(dto.provider(), dto.providerId())
                .orElseGet(() -> {
                    // 신규 회원가입
                    Member newMember = authMapper.toCreateEntity(dto);
                    newMember.addRole(RoleType.ROLE_USER);
                    return memberRepository.save(newMember);
                });

        // 내부 JWT 발급
        AuthTokens tokens = createAuthTokens(member);
        saveRefreshToken(member.getId(), tokens.refreshToken());

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

        if (!savedToken.equals(TokenHashUtil.hash(requestToken))) {
            refreshTokenRepository.deleteByMemberId(memberId);
            throw new MemberServiceException(MemberServiceErrorCode.INVALID_TOKEN_TYPE);
        }
    }

    private void saveRefreshToken(Long memberId, String refreshToken) {
        String hashedToken = TokenHashUtil.hash(refreshToken);
        refreshTokenRepository.save(memberId, hashedToken, jwtTokenProvider.getRefreshTokenExpireTime());
    }
}
