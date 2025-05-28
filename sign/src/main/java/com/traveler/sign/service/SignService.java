package com.traveler.sign.service;


import com.traveler.sign.dto.SignDto;
import com.traveler.sign.dto.SignInResultDto;
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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SignService {
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final KafkaProducerService kafkaProducerService;

    @Transactional
    public void signUp(String loginId, String password, String email, String nickname, String gender, String role){
        if (memberRepository.existsByLoginId(loginId)) {
            throw new CustomSignException("이미 가입되어 있는 유저입니다.");
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
        memberRepository.save(member);

    }
    @Transactional
    public void updateMember(SignDto data){
        Member member = memberRepository.findByLoginId(data.getLoginId()).orElseThrow(() -> new CustomSignException("존재하지 않는 유저입니다."));
        String prevNickname = member.getNickname();

        member.setPassword(passwordEncoder.encode(data.getPassword()));
        member.setNickname(data.getNickname());
        member.setGender(data.getGender());

        kafkaProducerService.updateNickname(prevNickname, member.getNickname());
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

    public String getNickname(String loginId){
        return memberRepository.findNicknameByLoginId(loginId).orElseThrow(() -> new CustomSignException("닉네임이 없습니다."));
    }

    public Map<Long, String> getNicknameList(Set<Long> memberIds){
        List<Member> members = memberRepository.findAllById(memberIds);
        // Map<Long, String> 으로 변환
        return members.stream()
                .collect(Collectors.toMap(Member::getId, Member::getNickname));

    }
}
