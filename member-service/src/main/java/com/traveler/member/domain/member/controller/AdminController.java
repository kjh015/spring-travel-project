package com.traveler.member.domain.member.controller;

import com.traveler.common.core.code.SuccessCode;
import com.traveler.common.core.response.ApiResponse;
import com.traveler.common.core.response.PageResponse;
import com.traveler.member.domain.member.dto.response.AdminResponse;
import com.traveler.member.domain.member.service.command.MemberCommandService;
import com.traveler.member.domain.member.service.query.MemberQueryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Admin", description = "Admin API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/admin/members")
public class AdminController {
    private final MemberQueryService memberQueryService;
    private final MemberCommandService memberCommandService;

    @PatchMapping("/{memberId}/role")
    public ApiResponse<AdminResponse.GrantAdminDTO> grantAdminRole(@PathVariable Long memberId) {
        return ApiResponse.onSuccess(SuccessCode.OK, memberCommandService.grantAdminRole(memberId));
    }

    @DeleteMapping("/{memberId}")
    public ApiResponse<AdminResponse.DeleteDTO> deleteMember(@PathVariable Long memberId) {
        return ApiResponse.onSuccess(SuccessCode.OK, memberCommandService.deleteMember(memberId));
    }

    @GetMapping
    public ApiResponse<PageResponse<AdminResponse.ListDTO>> getMembers(
            @ParameterObject @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
                    Pageable pageable) {
        return ApiResponse.onSuccess(SuccessCode.OK, memberQueryService.getMembers(pageable));
    }

    @GetMapping("/{memberId}")
    public ApiResponse<AdminResponse.DetailDTO> getMember(@PathVariable Long memberId) {
        return ApiResponse.onSuccess(SuccessCode.OK, memberQueryService.getMember(memberId));
    }
}
