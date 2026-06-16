package com.traveler.member.domain.member.service.query;

import com.traveler.member.domain.member.dto.response.MemberResponse;
import com.traveler.member.domain.member.entity.Member;
import com.traveler.member.domain.member.enums.AvailabilityType;
import com.traveler.member.domain.member.mapper.MemberMapper;
import com.traveler.member.domain.member.repository.MemberRepository;
import com.traveler.member.global.exception.MemberServiceException;
import com.traveler.member.global.exception.code.MemberServiceErrorCode;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
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
                .findByIdAndIsDeletedFalse(memberId)
                .orElseThrow(() -> new MemberServiceException(MemberServiceErrorCode.MEMBER_NOT_FOUND));

        return memberMapper.toProfileDTO(member, member.getAge());
    }

    public MemberResponse.MyProfileDTO getMyProfile(Long memberId) {
        Member member = memberRepository
                .findActiveByIdWithRoles(memberId)
                .orElseThrow(() -> new MemberServiceException(MemberServiceErrorCode.MEMBER_NOT_FOUND));

        return memberMapper.toMyProfileDTO(member, member.getAge());
    }

    public MemberResponse.AvailabilityDTO checkLoginIdAvailability(String loginId) {
        boolean isAvailable = !memberRepository.existsByLoginIdAndIsDeletedFalse(loginId);
        AvailabilityType reason = isAvailable ? AvailabilityType.AVAILABLE : AvailabilityType.DUPLICATED;
        return memberMapper.toAvailabilityDTO(isAvailable, loginId, reason);
    }

    public MemberResponse.AvailabilityDTO checkEmailAvailability(String email) {
        boolean isAvailable = !memberRepository.existsByEmailAndIsDeletedFalse(email);
        AvailabilityType reason = isAvailable ? AvailabilityType.AVAILABLE : AvailabilityType.DUPLICATED;
        return memberMapper.toAvailabilityDTO(isAvailable, email, reason);
    }

    public MemberResponse.AvailabilityDTO checkNicknameAvailability(String nickname) {
        boolean isAvailable = !memberRepository.existsByNicknameAndIsDeletedFalse(nickname);
        AvailabilityType reason = isAvailable ? AvailabilityType.AVAILABLE : AvailabilityType.DUPLICATED;
        return memberMapper.toAvailabilityDTO(isAvailable, nickname, reason);
    }

    public List<MemberResponse.ProfileDTO> getMemberProfiles(Set<Long> memberIds) {
        if (memberIds == null || memberIds.isEmpty()) {
            return List.of();
        }

        List<Member> members = memberRepository.findAllById(memberIds);

        return members.stream()
                .map(member -> memberMapper.toProfileDTO(member, member.getAge()))
                .toList();
    }
}
