package com.traveler.web.domain.useractivity.controller;

import com.traveler.common.api.auth.annotation.RequireRole;
import com.traveler.common.core.code.ErrorCode;
import com.traveler.common.core.code.SuccessCode;
import com.traveler.common.core.response.ApiResponse;
import com.traveler.common.core.response.PageResponse;
import com.traveler.web.domain.useractivity.dto.request.FormatRuleRequest;
import com.traveler.web.domain.useractivity.dto.response.FormatRuleResponse;
import com.traveler.web.domain.useractivity.facade.FormatRuleFacade;
import com.traveler.web.global.swagger.ApiErrorCodeExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@Tag(name = "FormatRule", description = "FormatRule API")
@RestController
@RequiredArgsConstructor
@RequireRole("ROLE_ADMIN")
@RequestMapping("/api/v1/admin")
public class FormatRuleController {
    private final FormatRuleFacade formatRuleFacade;

    @Operation(summary = "포맷 규칙 생성", description = "특정 로그 프로세스에 새로운 포맷 규칙을 생성합니다.")
    @ApiErrorCodeExamples(common = {ErrorCode.INVALID_TYPE_VALUE})
    @PostMapping("/log-processes/{logProcessId}/format-rules")
    public ApiResponse<FormatRuleResponse.CreateDTO> createFormatRule(
            @Parameter(description = "로그 프로세스 ID") @PathVariable Long logProcessId,
            @Valid @RequestBody FormatRuleRequest.CreateDTO dto) {
        return ApiResponse.onSuccess(SuccessCode.OK, formatRuleFacade.createFormatRule(logProcessId, dto));
    }

    @Operation(summary = "포맷 규칙 수정", description = "기존 포맷 규칙의 내용을 수정합니다.")
    @ApiErrorCodeExamples(common = {ErrorCode.INVALID_TYPE_VALUE})
    @PatchMapping("/format-rules/{formatRuleId}")
    public ApiResponse<FormatRuleResponse.UpdateDTO> updateFormatRule(
            @Parameter(description = "포맷 규칙 ID") @PathVariable Long formatRuleId,
            @Valid @RequestBody FormatRuleRequest.UpdateDTO dto) {
        return ApiResponse.onSuccess(SuccessCode.OK, formatRuleFacade.updateFormatRule(formatRuleId, dto));
    }

    @Operation(summary = "포맷 규칙 삭제", description = "포맷 규칙을 삭제(Soft Delete) 처리합니다.")
    @DeleteMapping("/format-rules/{formatRuleId}")
    public ApiResponse<FormatRuleResponse.DeleteDTO> deleteFormatRule(
            @Parameter(description = "포맷 규칙 ID") @PathVariable Long formatRuleId) {
        return ApiResponse.onSuccess(SuccessCode.OK, formatRuleFacade.deleteFormatRule(formatRuleId));
    }

    @Operation(summary = "포맷 규칙 목록 조회", description = "특정 로그 프로세스에 속한 포맷 규칙 목록을 조회합니다.")
    @GetMapping("/log-processes/{logProcessId}/format-rules")
    public ApiResponse<PageResponse<FormatRuleResponse.ListDTO>> getFormatRules(
            @Parameter(description = "로그 프로세스 ID") @PathVariable Long logProcessId,
            @ParameterObject @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
                    Pageable pageable) {
        return ApiResponse.onSuccess(SuccessCode.OK, formatRuleFacade.getFormatRules(logProcessId, pageable));
    }

    @Operation(summary = "포맷 규칙 상세 조회", description = "특정 포맷 규칙의 상세 정보를 조회합니다.")
    @GetMapping("/format-rules/{formatRuleId}")
    public ApiResponse<FormatRuleResponse.DetailDTO> getFormatRule(
            @Parameter(description = "포맷 규칙 ID") @PathVariable Long formatRuleId) {
        return ApiResponse.onSuccess(SuccessCode.OK, formatRuleFacade.getFormatRule(formatRuleId));
    }

    @Operation(summary = "활성 포맷 규칙 필드 조회", description = "특정 로그 프로세스에서 현재 활성화된 포맷 규칙의 필드 목록을 조회합니다.")
    @GetMapping("/log-processes/{logProcessId}/format-rules/fields")
    public ApiResponse<FormatRuleResponse.FieldDTO> getActiveFormatRuleFields(
            @Parameter(description = "로그 프로세스 ID") @PathVariable Long logProcessId) {
        return ApiResponse.onSuccess(SuccessCode.OK, formatRuleFacade.getActiveFormatRuleFields(logProcessId));
    }
}
