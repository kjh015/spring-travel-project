package com.traveler.member.domain.member.service.command;

import com.traveler.member.domain.member.dto.request.MemberRequest;
import com.traveler.member.domain.member.dto.response.AdminResponse;
import com.traveler.member.domain.member.dto.response.MemberResponse;
import com.traveler.member.domain.member.entity.Member;
import com.traveler.member.domain.member.enums.RoleType;
import com.traveler.member.domain.member.mapper.MemberMapper;
import com.traveler.member.domain.member.repository.MemberRepository;
import com.traveler.member.global.exception.MemberServiceException;
import com.traveler.member.global.exception.code.MemberServiceErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberCommandService {
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;

    public MemberResponse.SignUpDTO signUp(MemberRequest.SignUpDTO dto) {
        if (memberRepository.existsByLoginId(dto.loginId())) {
            throw new MemberServiceException(MemberServiceErrorCode.MEMBER_EXISTS_LOGINID);
        }

        if (memberRepository.existsByEmail(dto.email())) {
            throw new MemberServiceException(MemberServiceErrorCode.MEMBER_EXISTS_EMAIL);
        }

        if (memberRepository.existsByNickname(dto.nickname())) {
            throw new MemberServiceException(MemberServiceErrorCode.MEMBER_EXISTS_NICKNAME);
        }

        String encodedPassword = passwordEncoder.encode(dto.password());

        Member member = memberMapper.toCreateEntity(dto, encodedPassword);
        member.addRole(RoleType.ROLE_USER);
        try {
            // DB 강제 플러시 저장
            memberRepository.saveAndFlush(member);
        } catch (DataIntegrityViolationException e) {
            throw new MemberServiceException(MemberServiceErrorCode.MEMBER_ALREADY_EXISTS);
        }
        return memberMapper.toSignUpDTO(member);
    }

    public MemberResponse.WithdrawDTO withdraw(Long memberId) {
        Member member = memberRepository
                .findById(memberId)
                .orElseThrow(() -> new MemberServiceException(MemberServiceErrorCode.MEMBER_NOT_FOUND));
        member.delete();
        return memberMapper.toWithdrawDTO(member);
    }

    public MemberResponse.UpdateDTO updateMember(MemberRequest.UpdateDTO dto, Long memberId) {
        Member member = memberRepository
                .findById(memberId)
                .orElseThrow(() -> new MemberServiceException(MemberServiceErrorCode.MEMBER_NOT_FOUND));

        if (!member.getNickname().equals(dto.nickname()) && memberRepository.existsByNickname(dto.nickname())) {
            throw new MemberServiceException(MemberServiceErrorCode.MEMBER_EXISTS_NICKNAME);
        }

        member.update(dto.nickname());
        return memberMapper.toUpdateDTO(member);
    }

    public MemberResponse.UpdatePasswordDTO updatePassword(MemberRequest.UpdatePasswordDTO dto, Long memberId) {
        Member member = memberRepository
                .findById(memberId)
                .orElseThrow(() -> new MemberServiceException(MemberServiceErrorCode.MEMBER_NOT_FOUND));

        if (!passwordEncoder.matches(dto.curPassword(), member.getPassword())) {
            throw new MemberServiceException(MemberServiceErrorCode.INVALID_PASSWORD);
        }

        if (passwordEncoder.matches(dto.newPassword(), member.getPassword())) {
            throw new MemberServiceException(MemberServiceErrorCode.PASSWORD_SAME_AS_OLD);
        }

        String encodedNewPassword = passwordEncoder.encode(dto.newPassword());
        member.updatePassword(encodedNewPassword);

        return memberMapper.toUpdatePasswordDTO(member);
    }

    public AdminResponse.GrantAdminDTO grantAdminRole(Long memberId) {
        Member member = memberRepository
                .findByIdWithRoles(memberId)
                .orElseThrow(() -> new MemberServiceException(MemberServiceErrorCode.MEMBER_NOT_FOUND));

        member.addRole(RoleType.ROLE_ADMIN);

        return memberMapper.toGrantAdminDTO(member);
    }

    public AdminResponse.DeleteDTO deleteMember(Long memberId) {
        Member member = memberRepository
                .findById(memberId)
                .orElseThrow(() -> new MemberServiceException(MemberServiceErrorCode.MEMBER_NOT_FOUND));

        member.delete();

        return memberMapper.toDeleteDTO(member);
    }
}
