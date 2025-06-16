package com.traveler.sign.service;


import com.traveler.sign.dto.PasswordDto;
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

import java.util.*;
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
    public void signUp(String loginId, String password, String email, String nickname, String gender){
        if (memberRepository.existsByLoginId(loginId)) {
            throw new CustomSignException("이미 가입되어 있는 유저입니다.");
        }

        Member member = Member.builder()
                .loginId(loginId)
                .password(passwordEncoder.encode(password))
                .email(email)
                .nickname(nickname)
                .gender(gender)
                .roles(Collections.singletonList("ROLE_ADMIN"))
                .build();

        memberRepository.save(member);

    }
    @Transactional
    public void updateMember(SignDto data){
        Member member = memberRepository.findByLoginId(data.getLoginId()).orElseThrow(() -> new CustomSignException("존재하지 않는 유저입니다."));

        member.setNickname(data.getNickname());
        member.setGender(data.getGender());
    }

    @Transactional
    public void updatePassword(PasswordDto data){
        Member member = memberRepository.findByLoginId(data.getLoginId()).orElseThrow(() -> new CustomSignException("존재하지 않는 유저입니다."));
        if(!passwordEncoder.matches(data.getCurPassword(), member.getPassword())) {
            throw new CustomSignException("비밀번호가 일치하지 않습니다.");
        }
        member.setPassword(passwordEncoder.encode(data.getNewPassword()));
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

        Optional<RefreshToken> tokenOpt = refreshTokenRepository.findByLoginId(loginId);
        if(tokenOpt.isPresent()){
            RefreshToken refreshToken = tokenOpt.get();
            refreshToken.setRefreshToken(signInResultDto.getRefreshToken());
            refreshTokenRepository.save(refreshToken);
        }
        else{
            RefreshToken refreshToken = new RefreshToken();
            refreshToken.setLoginId(loginId);
            refreshToken.setRefreshToken(signInResultDto.getRefreshToken());
            refreshTokenRepository.save(refreshToken);
        }



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

    public SignDto getMemberDetail(String loginId){
        Member member = memberRepository.findByLoginId(loginId).orElseThrow(() -> new CustomSignException("존재하지 않는 회원입니다."));
        return SignDto.builder()
                .loginId(member.getLoginId())
                .email(member.getEmail())
                .gender(member.getGender())
                .nickname(member.getNickname())
                .roles(member.getRoles())
                .regDate(member.getRegDate())
                .build();
    }

    public List<SignDto> getMemberList(){
        List<Member> memberList = memberRepository.findAll();
        return memberList.stream().map(member -> SignDto.builder()
                .id(member.getId())
                .loginId(member.getLoginId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .gender(member.getGender())
                .roles(member.getRoles())
                .regDate(member.getRegDate())
                .build()
        ).collect(Collectors.toList());
    }

    @Transactional
    public void delegateAdmin(String loginId){
        Member member = memberRepository.findByLoginId(loginId).orElseThrow(() -> new CustomSignException("존재하지 않는 회원입니다."));
        member.getRoles().add("ROLE_ADMIN");
        memberRepository.save(member);
    }

    public String getNicknameByLoginId(String loginId){
        return memberRepository.findNicknameByLoginId(loginId).orElseThrow(() -> new CustomSignException("닉네임이 없습니다."));
    }
    public String getNicknameById(Long id){
        return memberRepository.findNicknameById(id).orElse(null);
    }
    public Long getIdByNickname(String nickname){
        return memberRepository.findIdByNickname(nickname).orElse(null);
    }

    public Map<Long, String> getNicknameList(Set<Long> memberIds){
        List<Member> members = memberRepository.findAllById(memberIds);
        // Map<Long, String> 으로 변환
        return members.stream()
                .collect(Collectors.toMap(Member::getId, Member::getNickname));

    }
}
