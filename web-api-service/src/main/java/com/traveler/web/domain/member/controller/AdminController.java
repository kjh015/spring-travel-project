package com.traveler.web.domain.member.controller;

import com.traveler.common.core.code.ErrorCode;
import com.traveler.common.core.code.SuccessCode;
import com.traveler.common.core.response.ApiResponse;
import com.traveler.common.core.response.PageResponse;
import com.traveler.web.domain.member.dto.response.AdminResponse;
import com.traveler.web.domain.member.facade.AdminFacade;
import com.traveler.web.global.swagger.ApiErrorCodeExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Admin API (Web)", description = "웹 클라이언트 어드민 전용 회원 관리 API (BFF)")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/members")
public class AdminController {
    private final AdminFacade adminFacade;

    @Operation(summary = "관리자 권한 부여", description = "Member 서버에 특정 회원의 시스템 관리자(ROLE_ADMIN) 권한 부여를 요청합니다.")
    @ApiErrorCodeExamples(common = {ErrorCode.INVALID_TYPE_VALUE})
    @PatchMapping("/{memberId}/role")
    public ApiResponse<AdminResponse.GrantAdminDTO> grantAdminRole(
            @Parameter(description = "대상 회원 ID") @Positive(message = "회원 ID는 양수여야 합니다.") @PathVariable Long memberId) {
        return ApiResponse.onSuccess(SuccessCode.OK, adminFacade.grantAdminRole(memberId));
    }

    @Operation(summary = "회원 강제 탈퇴", description = "Member 서버에 특정 회원의 강제 탈퇴(논리적 삭제)를 요청합니다.")
    @ApiErrorCodeExamples(common = {ErrorCode.INVALID_TYPE_VALUE})
    @DeleteMapping("/{memberId}")
    public ApiResponse<AdminResponse.DeleteDTO> deleteMember(
            @Parameter(description = "강제 탈퇴할 회원 ID") @Positive(message = "회원 ID는 양수여야 합니다.") @PathVariable
                    Long memberId) {
        return ApiResponse.onSuccess(SuccessCode.OK, adminFacade.deleteMember(memberId));
    }

    @Operation(summary = "전체 회원 목록 조회", description = "어드민 패널에 표시할 전체 회원 목록을 페이징하여 조회합니다.")
    @GetMapping
    public ApiResponse<PageResponse<AdminResponse.ListDTO>> getMembers(
            @ParameterObject @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
                    Pageable pageable) {
        return ApiResponse.onSuccess(SuccessCode.OK, adminFacade.getMembers(pageable));
    }

    @Operation(summary = "회원 상세 정보 조회", description = "어드민 패널에서 특정 회원의 상세 정보(탈퇴 여부 포함)를 조회합니다.")
    @ApiErrorCodeExamples(common = {ErrorCode.INVALID_TYPE_VALUE})
    @GetMapping("/{memberId}")
    public ApiResponse<AdminResponse.DetailDTO> getMember(
            @Parameter(description = "조회할 회원 ID") @Positive(message = "회원 ID는 양수여야 합니다.") @PathVariable Long memberId) {
        return ApiResponse.onSuccess(SuccessCode.OK, adminFacade.getMember(memberId));
    }
}
