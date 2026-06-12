package com.traveler.web.domain.member.facade;

import com.traveler.web.domain.member.adaptor.MemberClientAdaptor;
import com.traveler.web.domain.member.client.dto.request.MemberClientRequest;
import com.traveler.web.domain.member.client.dto.response.MemberClientResponse;
import com.traveler.web.domain.member.dto.request.MemberRequest;
import com.traveler.web.domain.member.dto.response.MemberResponse;
import com.traveler.web.domain.member.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberFacade {
    private final MemberClientAdaptor memberClientAdaptor;
    private final MemberMapper memberMapper;

    public MemberResponse.SignUpDTO signUp(MemberRequest.SignUpDTO dto) {
        MemberClientRequest.SignUpDTO clientRequest = memberMapper.toClientSignUpDTO(dto);
        MemberClientResponse.SignUpDTO clientResponse = memberClientAdaptor.signUp(clientRequest);
        return memberMapper.toResponseSignUpDTO(clientResponse);
    }

    public MemberResponse.WithdrawDTO withdraw() {
        MemberClientResponse.WithdrawDTO clientResponse = memberClientAdaptor.withdraw();
        return memberMapper.toResponseWithdrawDTO(clientResponse);
    }

    public MemberResponse.UpdateDTO updateMember(MemberRequest.UpdateDTO dto) {
        MemberClientRequest.UpdateDTO clientRequest = memberMapper.toClientUpdateDTO(dto);
        MemberClientResponse.UpdateDTO clientResponse = memberClientAdaptor.updateMember(clientRequest);
        return memberMapper.toResponseUpdateDTO(clientResponse);
    }

    public MemberResponse.UpdatePasswordDTO updatePassword(MemberRequest.UpdatePasswordDTO dto) {
        MemberClientRequest.UpdatePasswordDTO clientRequest = memberMapper.toClientUpdatePasswordDTO(dto);
        MemberClientResponse.UpdatePasswordDTO clientResponse = memberClientAdaptor.updatePassword(clientRequest);
        return memberMapper.toResponseUpdatePasswordDTO(clientResponse);
    }

    public MemberResponse.MyProfileDTO getMyProfile() {
        MemberClientResponse.MyProfileDTO clientResponse = memberClientAdaptor.getMyProfile();
        return memberMapper.toResponseMyProfileDTO(clientResponse);
    }

    public MemberResponse.AvailabilityDTO checkLoginIdAvailability(String loginId) {
        MemberClientResponse.AvailabilityDTO clientResponse = memberClientAdaptor.checkLoginIdAvailability(loginId);
        return memberMapper.toResponseAvailabilityDTO(clientResponse);
    }

    public MemberResponse.AvailabilityDTO checkEmailAvailability(String email) {
        MemberClientResponse.AvailabilityDTO clientResponse = memberClientAdaptor.checkEmailAvailability(email);
        return memberMapper.toResponseAvailabilityDTO(clientResponse);
    }

    public MemberResponse.AvailabilityDTO checkNicknameAvailability(String nickname) {
        MemberClientResponse.AvailabilityDTO clientResponse = memberClientAdaptor.checkNicknameAvailability(nickname);
        return memberMapper.toResponseAvailabilityDTO(clientResponse);
    }
}
