package com.traveler.member.domain.member.controller;

import com.traveler.common.core.code.ErrorCode;
import com.traveler.common.core.code.SuccessCode;
import com.traveler.common.core.response.ApiResponse;
import com.traveler.common.core.response.PageResponse;
import com.traveler.member.domain.member.dto.response.AdminResponse;
import com.traveler.member.domain.member.service.command.MemberCommandService;
import com.traveler.member.domain.member.service.query.MemberQueryService;
import com.traveler.member.global.exception.code.MemberServiceErrorCode;
import com.traveler.member.global.swagger.ApiErrorCodeExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Admin API", description = "어드민 전용 회원 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/admin/members")
public class AdminController {
    private final MemberQueryService memberQueryService;
    private final MemberCommandService memberCommandService;

    @Operation(summary = "관리자 권한 부여", description = "특정 회원에게 시스템 관리자(ROLE_ADMIN) 권한을 부여합니다.")
    @ApiErrorCodeExamples(
            value = {MemberServiceErrorCode.MEMBER_NOT_FOUND},
            common = {ErrorCode.INVALID_TYPE_VALUE})
    @PatchMapping("/{memberId}/role")
    public ApiResponse<AdminResponse.GrantAdminDTO> grantAdminRole(
            @Parameter(description = "대상 회원 ID") @PathVariable Long memberId) {
        return ApiResponse.onSuccess(SuccessCode.OK, memberCommandService.grantAdminRole(memberId));
    }

    @Operation(summary = "회원 강제 탈퇴", description = "특정 회원을 강제 탈퇴(논리적 삭제) 처리합니다.")
    @ApiErrorCodeExamples(
            value = {MemberServiceErrorCode.MEMBER_NOT_FOUND, MemberServiceErrorCode.MEMBER_ALREADY_DELETED},
            common = {ErrorCode.INVALID_TYPE_VALUE})
    @DeleteMapping("/{memberId}")
    public ApiResponse<AdminResponse.DeleteDTO> deleteMember(
            @Parameter(description = "강제 탈퇴할 회원 ID") @PathVariable Long memberId) {
        return ApiResponse.onSuccess(SuccessCode.OK, memberCommandService.deleteMember(memberId));
    }

    @Operation(summary = "전체 회원 목록 조회", description = "전체 회원의 목록을 페이징하여 조회합니다.")
    @GetMapping
    public ApiResponse<PageResponse<AdminResponse.ListDTO>> getMembers(
            @ParameterObject @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
                    Pageable pageable) {
        return ApiResponse.onSuccess(SuccessCode.OK, memberQueryService.getMembers(pageable));
    }

    @Operation(summary = "회원 상세 정보 조회", description = "특정 회원의 상세 정보(탈퇴 여부 포함)를 조회합니다.")
    @ApiErrorCodeExamples(
            value = {MemberServiceErrorCode.MEMBER_NOT_FOUND},
            common = {ErrorCode.INVALID_TYPE_VALUE})
    @GetMapping("/{memberId}")
    public ApiResponse<AdminResponse.DetailDTO> getMember(
            @Parameter(description = "조회할 회원 ID") @PathVariable Long memberId) {
        return ApiResponse.onSuccess(SuccessCode.OK, memberQueryService.getMember(memberId));
    }
}
