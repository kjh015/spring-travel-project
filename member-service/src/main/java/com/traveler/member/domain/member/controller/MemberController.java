package com.traveler.member.domain.member.controller;

import com.traveler.common.api.auth.resolver.LoginUser;
import com.traveler.common.core.auth.UserContext;
import com.traveler.common.core.code.SuccessCode;
import com.traveler.common.core.response.ApiResponse;
import com.traveler.member.domain.member.dto.request.MemberRequest;
import com.traveler.member.domain.member.dto.response.MemberResponse;
import com.traveler.member.domain.member.service.command.MemberCommandService;
import com.traveler.member.domain.member.service.query.MemberQueryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Member", description = "Member API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/members")
public class MemberController {
    private final MemberQueryService memberQueryService;
    private final MemberCommandService memberCommandService;

    @PostMapping()
    public ApiResponse<MemberResponse.SignUpDTO> signUp(@RequestBody MemberRequest.SignUpDTO dto) {
        return ApiResponse.onSuccess(SuccessCode.OK, memberCommandService.signUp(dto));
    }

    @DeleteMapping("/me")
    public ApiResponse<MemberResponse.WithdrawDTO> withdraw(@LoginUser UserContext user) {
        return ApiResponse.onSuccess(SuccessCode.OK, memberCommandService.withdraw(user.id()));
    }

    @PatchMapping("/me")
    public ApiResponse<MemberResponse.UpdateDTO> updateMember(
            @RequestBody MemberRequest.UpdateDTO dto, @LoginUser UserContext user) {
        return ApiResponse.onSuccess(SuccessCode.OK, memberCommandService.updateMember(dto, user.id()));
    }

    @PatchMapping("/me/password")
    public ApiResponse<MemberResponse.UpdatePasswordDTO> updatePassword(
            @RequestBody MemberRequest.UpdatePasswordDTO dto, @LoginUser UserContext user) {
        return ApiResponse.onSuccess(SuccessCode.OK, memberCommandService.updatePassword(dto, user.id()));
    }

    @GetMapping
    public ApiResponse<List<MemberResponse.ProfileDTO>> getMemberProfiles(@RequestParam Set<Long> memberIds) {
        return ApiResponse.onSuccess(SuccessCode.OK, memberQueryService.getMemberProfiles(memberIds));
    }

    @GetMapping("/{memberId}")
    public ApiResponse<MemberResponse.ProfileDTO> getMemberProfile(@PathVariable Long memberId) {
        return ApiResponse.onSuccess(SuccessCode.OK, memberQueryService.getMemberProfile(memberId));
    }

    @GetMapping("/me")
    public ApiResponse<MemberResponse.MyProfileDTO> getMyProfile(@LoginUser UserContext user) {
        return ApiResponse.onSuccess(SuccessCode.OK, memberQueryService.getMyProfile(user.id()));
    }

    @GetMapping("/availability/login-id")
    public ApiResponse<MemberResponse.AvailabilityDTO> checkLoginIdAvailability(@RequestParam String loginId) {
        return ApiResponse.onSuccess(SuccessCode.OK, memberQueryService.checkLoginIdAvailability(loginId));
    }

    @GetMapping("/availability/email")
    public ApiResponse<MemberResponse.AvailabilityDTO> checkEmailAvailability(@RequestParam String email) {
        return ApiResponse.onSuccess(SuccessCode.OK, memberQueryService.checkEmailAvailability(email));
    }

    @GetMapping("/availability/nickname")
    public ApiResponse<MemberResponse.AvailabilityDTO> checkNicknameAvailability(@RequestParam String nickname) {
        return ApiResponse.onSuccess(SuccessCode.OK, memberQueryService.checkNicknameAvailability(nickname));
    }
}
