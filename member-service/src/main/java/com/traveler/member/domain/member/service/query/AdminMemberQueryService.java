package com.traveler.member.domain.member.service.query;

import com.traveler.common.api.converter.PageConverter;
import com.traveler.common.core.response.PageResponse;
import com.traveler.member.domain.member.dto.response.AdminMemberResponse;
import com.traveler.member.domain.member.entity.Member;
import com.traveler.member.domain.member.mapper.MemberMapper;
import com.traveler.member.domain.member.repository.MemberRepository;
import com.traveler.member.global.exception.MemberServiceException;
import com.traveler.member.global.exception.code.MemberServiceErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminMemberQueryService {
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    public PageResponse<AdminMemberResponse.ListDTO> getMembers(Pageable pageable) {
        // BatchSize로 수정필요
        Page<Member> members = memberRepository.findAll(pageable);
        return PageConverter.toPageResponse(members, memberMapper::toListDTO);
    }

    public AdminMemberResponse.DetailDTO getMember(Long memberId) {
        Member member = memberRepository
                .findByIdWithRoles(memberId)
                .orElseThrow(() -> new MemberServiceException(MemberServiceErrorCode.MEMBER_NOT_FOUND));

        return memberMapper.toDetailDTO(member);
    }
}
