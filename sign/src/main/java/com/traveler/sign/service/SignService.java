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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SignService {
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public SignUpResultDto signUp(String loginId, String password, String email, String nickname, String gender, String role){
        if (memberRepository.existsByLoginId(loginId)) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다");
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
        Member member = memberRepository.getByLoginId(loginId);

        if(!passwordEncoder.matches(password, member.getPassword())) {
            throw new RuntimeException();
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

    public Member getMember(String loginId){
        Optional<Member> _member = memberRepository.findByLoginId(loginId);
        if(_member.isPresent()){
            return _member.get();
        }
        else{
            throw new RuntimeException();
        }
    }
}
