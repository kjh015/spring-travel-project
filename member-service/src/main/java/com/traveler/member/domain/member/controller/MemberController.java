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
        return ApiResponse.onSuccess(SuccessCode.OK, null);
    }

    @DeleteMapping("/me")
    public ApiResponse<MemberResponse.WithdrawDTO> withdraw(@LoginUser UserContext user) {
        return ApiResponse.onSuccess(SuccessCode.OK, null);
    }

    @PatchMapping("/me")
    public ApiResponse<MemberResponse.UpdateDTO> updateMember(@LoginUser UserContext user) {
        return ApiResponse.onSuccess(SuccessCode.OK, null);
    }

    @PatchMapping("/me/password")
    public ApiResponse<MemberResponse.UpdatePasswordDTO> updatePassword(@LoginUser UserContext user) {
        return ApiResponse.onSuccess(SuccessCode.OK, null);
    }

    @GetMapping()
    public ApiResponse<MemberResponse.ListDTO> getMembers() {
        return ApiResponse.onSuccess(SuccessCode.OK, null);
    }

    @GetMapping("/{memberId}")
    public ApiResponse<MemberResponse.DetailDTO> getMember(@PathVariable String memberId) {
        return ApiResponse.onSuccess(SuccessCode.OK, null);
    }

    @GetMapping("/{memberId}/nickname")
    public ApiResponse<MemberResponse.NicknameDTO> getNickname(@PathVariable String memberId) {
        return ApiResponse.onSuccess(SuccessCode.OK, null);
    }

    @GetMapping("/check")
    public ApiResponse<MemberResponse.CheckDTO> checkDuplicate() {
        return ApiResponse.onSuccess(SuccessCode.OK, null);
    }
}
