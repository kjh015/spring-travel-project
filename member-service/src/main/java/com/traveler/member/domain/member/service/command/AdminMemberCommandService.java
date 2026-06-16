package com.traveler.member.domain.member.service.command;

import com.traveler.member.domain.member.dto.response.AdminMemberResponse;
import com.traveler.member.domain.member.entity.Member;
import com.traveler.member.domain.member.enums.RoleType;
import com.traveler.member.domain.member.mapper.MemberMapper;
import com.traveler.member.domain.member.repository.MemberRepository;
import com.traveler.member.global.exception.MemberServiceException;
import com.traveler.member.global.exception.code.MemberServiceErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminMemberCommandService {
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    public AdminMemberResponse.GrantAdminDTO grantAdminRole(Long memberId) {
        Member member = memberRepository
                .findByIdWithRoles(memberId)
                .orElseThrow(() -> new MemberServiceException(MemberServiceErrorCode.MEMBER_NOT_FOUND));

        member.addRole(RoleType.ROLE_ADMIN);

        return memberMapper.toGrantAdminDTO(member);
    }

    public AdminMemberResponse.DeleteDTO deleteMember(Long memberId) {
        Member member = memberRepository
                .findById(memberId)
                .orElseThrow(() -> new MemberServiceException(MemberServiceErrorCode.MEMBER_NOT_FOUND));

        member.delete();

        return memberMapper.toDeleteDTO(member);
    }
}
