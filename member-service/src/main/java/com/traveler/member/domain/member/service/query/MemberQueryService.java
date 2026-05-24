package com.traveler.member.domain.member.service.query;

import com.traveler.common.api.converter.PageConverter;
import com.traveler.common.core.response.PageResponse;
import com.traveler.member.domain.member.dto.response.AdminResponse;
import com.traveler.member.domain.member.dto.response.MemberResponse;
import com.traveler.member.domain.member.entity.Member;
import com.traveler.member.domain.member.enums.AvailabilityType;
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
public class MemberQueryService {
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    public MemberResponse.ProfileDTO getMemberProfile(Long memberId) {
        Member member = memberRepository
                .findById(memberId)
                .orElseThrow(() -> new MemberServiceException(MemberServiceErrorCode.MEMBER_NOT_FOUND));

        return memberMapper.toProfileDTO(member, member.getAge());
    }

    public MemberResponse.MyProfileDTO getMyProfile(Long memberId) {
        Member member = memberRepository
                .findById(memberId)
                .orElseThrow(() -> new MemberServiceException(MemberServiceErrorCode.MEMBER_NOT_FOUND));

        return memberMapper.toMyProfileDTO(member, member.getAge());
    }

    public MemberResponse.AvailabilityDTO checkLoginIdAvailability(String loginId) {
        boolean isAvailable = !memberRepository.existsByLoginId(loginId);
        return memberMapper.AvailabilityDTO(isAvailable, loginId, AvailabilityType.DUPLICATED);
    }

    public MemberResponse.AvailabilityDTO checkEmailAvailability(String email) {
        boolean isAvailable = !memberRepository.existsByEmail(email);
        return memberMapper.AvailabilityDTO(isAvailable, email, AvailabilityType.DUPLICATED);
    }

    public MemberResponse.AvailabilityDTO checkNicknameAvailability(String nickname) {
        boolean isAvailable = !memberRepository.existsByNickname(nickname);
        return memberMapper.AvailabilityDTO(isAvailable, nickname, AvailabilityType.DUPLICATED);
    }

    public PageResponse<AdminResponse.ListDTO> getMembers(Pageable pageable) {
        Page<Member> members = memberRepository.findAll(pageable);
        return PageConverter.toPageResponse(members, memberMapper::toListDTO);
    }

    public AdminResponse.DetailDTO getMember(Long memberId) {
        Member member = memberRepository
                .findById(memberId)
                .orElseThrow(() -> new MemberServiceException(MemberServiceErrorCode.MEMBER_NOT_FOUND));

        return memberMapper.toDetailDTO(member);
    }
}
