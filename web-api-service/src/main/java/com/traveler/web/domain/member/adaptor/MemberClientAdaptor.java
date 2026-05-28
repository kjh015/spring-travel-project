package com.traveler.web.domain.member.adaptor;

import com.traveler.web.domain.member.client.MemberClient;
import com.traveler.web.domain.member.client.dto.request.MemberClientRequest;
import com.traveler.web.domain.member.client.dto.response.MemberClientResponse;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberClientAdaptor {
    private final MemberClient memberClient;

    public MemberClientResponse.SignUpDTO signUp(MemberClientRequest.SignUpDTO dto) {
        return memberClient.signUp(dto).result();
    }

    public MemberClientResponse.WithdrawDTO withdraw() {
        return memberClient.withdraw().result();
    }

    public MemberClientResponse.UpdateDTO updateMember(MemberClientRequest.UpdateDTO dto) {
        return memberClient.updateMember(dto).result();
    }

    public MemberClientResponse.UpdatePasswordDTO updatePassword(MemberClientRequest.UpdatePasswordDTO dto) {
        return memberClient.updatePassword(dto).result();
    }

    public MemberClientResponse.MyProfileDTO getMyProfile() {
        return memberClient.getMyProfile().result();
    }

    public MemberClientResponse.AvailabilityDTO checkLoginIdAvailability(String loginId) {
        return memberClient.checkLoginIdAvailability(loginId).result();
    }

    public MemberClientResponse.AvailabilityDTO checkEmailAvailability(String email) {
        return memberClient.checkEmailAvailability(email).result();
    }

    public MemberClientResponse.AvailabilityDTO checkNicknameAvailability(String nickname) {
        return memberClient.checkNicknameAvailability(nickname).result();
    }

    public Map<Long, String> getNicknameMap(Set<Long> memberIds) {
        if (memberIds == null || memberIds.isEmpty()) {
            return Collections.emptyMap();
        }

        List<MemberClientResponse.ProfileDTO> profiles =
                memberClient.getMemberProfiles(memberIds).result();

        return profiles.stream()
                .collect(Collectors.toMap(
                        MemberClientResponse.ProfileDTO::memberId,
                        MemberClientResponse.ProfileDTO::nickname,
                        (existing, replacement) -> existing));
    }

    public String getMemberNickname(Long memberId) {
        return memberClient.getMemberProfile(memberId).result().nickname();
    }

    public String getMyNickname() {
        return memberClient.getMyProfile().result().nickname();
    }
}
