package com.traveler.web.domain.member.controller;

import com.traveler.common.core.code.SuccessCode;
import com.traveler.common.core.response.ApiResponse;
import com.traveler.web.domain.member.dto.request.MemberRequest;
import com.traveler.web.domain.member.dto.response.MemberResponse;
import com.traveler.web.domain.member.facade.MemberFacade;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Member", description = "Member API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {
    private final MemberFacade memberFacade;

    @PostMapping()
    public ApiResponse<MemberResponse.SignUpDTO> signUp(@RequestBody MemberRequest.SignUpDTO dto) {
        return ApiResponse.onSuccess(SuccessCode.OK, memberFacade.signUp(dto));
    }

    @DeleteMapping("/me")
    public ApiResponse<MemberResponse.WithdrawDTO> withdraw() {
        return ApiResponse.onSuccess(SuccessCode.OK, memberFacade.withdraw());
    }

    @PatchMapping("/me")
    public ApiResponse<MemberResponse.UpdateDTO> updateMember(@RequestBody MemberRequest.UpdateDTO dto) {
        return ApiResponse.onSuccess(SuccessCode.OK, memberFacade.updateMember(dto));
    }

    @PatchMapping("/me/password")
    public ApiResponse<MemberResponse.UpdatePasswordDTO> updatePassword(
            @RequestBody MemberRequest.UpdatePasswordDTO dto) {
        return ApiResponse.onSuccess(SuccessCode.OK, memberFacade.updatePassword(dto));
    }

    @GetMapping("/me")
    public ApiResponse<MemberResponse.MyProfileDTO> getMyProfile() {
        return ApiResponse.onSuccess(SuccessCode.OK, memberFacade.getMyProfile());
    }

    @GetMapping("/availability/login-id")
    public ApiResponse<MemberResponse.AvailabilityDTO> checkLoginIdAvailability(@RequestParam String loginId) {
        return ApiResponse.onSuccess(SuccessCode.OK, memberFacade.checkLoginIdAvailability(loginId));
    }

    @GetMapping("/availability/email")
    public ApiResponse<MemberResponse.AvailabilityDTO> checkEmailAvailability(@RequestParam String email) {
        return ApiResponse.onSuccess(SuccessCode.OK, memberFacade.checkEmailAvailability(email));
    }

    @GetMapping("/availability/nickname")
    public ApiResponse<MemberResponse.AvailabilityDTO> checkNicknameAvailability(@RequestParam String nickname) {
        return ApiResponse.onSuccess(SuccessCode.OK, memberFacade.checkNicknameAvailability(nickname));
    }
}
