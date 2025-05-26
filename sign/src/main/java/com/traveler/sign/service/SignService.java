package com.traveler.sign.service;


import com.traveler.sign.common.CommonResponse;
import com.traveler.sign.dto.SignInResultDto;
import com.traveler.sign.dto.SignUpResultDto;
import com.traveler.sign.entity.Member;
import com.traveler.sign.entity.RefreshToken;
import com.traveler.sign.repository.MemberRepository;
import com.traveler.sign.repository.RefreshTokenRepository;
import com.traveler.sign.security.JwtTokenProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class SignService {
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @Transactional
    public SignUpResultDto signUp(String loginId, String password, String email, String nickname, String gender, String role){
        if (memberRepository.existsByLoginId(loginId)) {
            throw new CustomSignException("이미 가입되어 있는 유저입니다");
        }

        Member member;

        if(role.equalsIgnoreCase("admin")){
            member = Member.builder().loginId(loginId).password(passwordEncoder.encode(password)).email(email).nickname(nickname)
                    .gender(gender).roles(Collections.singletonList("ROLE_ADMIN")).build();
        }
        else{
            member = Member.builder().loginId(loginId).password(passwordEncoder.encode(password)).email(email).nickname(nickname)
                    .gender(gender).roles(Collections.singletonList("ROLE_USER")).build();
        }
        Member savedMember = memberRepository.save(member);
        SignUpResultDto signUpResultDto = new SignUpResultDto();

        if(!savedMember.getLoginId().isEmpty()){
            setSuccessResult(signUpResultDto);
        }
        else{
            setFailResult(signUpResultDto);
        }
        return signUpResultDto;
    }
    @Transactional
    public SignInResultDto signIn(String loginId, String password) throws RuntimeException {
        Member member = memberRepository.findByLoginId(loginId).orElseThrow(() -> new CustomSignException("존재하지 않는 ID 입니다."));

        if(!passwordEncoder.matches(password, member.getPassword())) {
            throw new CustomSignException("비밀번호가 일치하지 않습니다.");
        }
        SignInResultDto signInResultDto = SignInResultDto.builder()
                .accessToken(jwtTokenProvider.createAccessToken(String.valueOf(member.getLoginId()), member.getRoles()))
                .refreshToken(jwtTokenProvider.createRefreshToken(String.valueOf(member.getLoginId()), member.getRoles()))
                .build(); // null일 경우 대비하여 String.valueOf()로 감쌈
        setSuccessResult(signInResultDto);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setLoginId(loginId);
        refreshToken.setRefreshToken(signInResultDto.getRefreshToken());
        refreshTokenRepository.save(refreshToken);

        return signInResultDto;
    }
    @Transactional
    public void signOut(String refreshToken) {
        if (refreshToken != null && jwtTokenProvider.validateToken(refreshToken)) {
            String loginId = jwtTokenProvider.getUsername(refreshToken);
            refreshTokenRepository.deleteByLoginId(loginId);
        }
    }

    @Transactional
    public void withdraw(String refreshToken){
        if (refreshToken != null && jwtTokenProvider.validateToken(refreshToken)) {
            String loginId = jwtTokenProvider.getUsername(refreshToken);
            Member member = memberRepository.findByLoginId(loginId).orElseThrow(() -> new CustomSignException("이미 삭제된 회원이거나 존재하지 않는 회원입니다."));
            refreshTokenRepository.deleteByLoginId(member.getLoginId());
            memberRepository.deleteById(member.getId());
        }
    }

    private void setSuccessResult(SignUpResultDto result) {
        result.setSuccess(true);
        result.setCode(CommonResponse.SUCCESS.getCode());
        result.setMsg(CommonResponse.SUCCESS.getMsg());

    }

    private void setFailResult(SignUpResultDto result) {
        result.setSuccess(false);
        result.setCode(CommonResponse.FAIL.getCode());
        result.setMsg(CommonResponse.FAIL.getMsg());
    }

    public String getNickname(String loginId){
        return memberRepository.findNicknameByLoginId(loginId).orElseThrow(() -> new CustomSignException("닉네임이 없습니다."));
    }
}
