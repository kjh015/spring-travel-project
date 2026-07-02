package com.traveler.web.domain.useractivity.controller;

import com.traveler.common.api.auth.annotation.RequireRole;
import com.traveler.common.core.code.ErrorCode;
import com.traveler.common.core.code.SuccessCode;
import com.traveler.common.core.response.ApiResponse;
import com.traveler.common.core.response.PageResponse;
import com.traveler.web.domain.useractivity.dto.request.DedupRuleRequest;
import com.traveler.web.domain.useractivity.dto.response.DedupRuleResponse;
import com.traveler.web.domain.useractivity.facade.DedupRuleFacade;
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

@Tag(name = "DedupRule", description = "DedupRule API")
@RestController
@RequiredArgsConstructor
@RequireRole("ROLE_ADMIN")
@RequestMapping("/api/v1/admin")
public class DedupRuleController {
    private final DedupRuleFacade dedupRuleFacade;

    @Operation(summary = "중복제거 규칙 생성", description = "특정 로그 프로세스에 새로운 중복제거 규칙을 생성합니다.")
    @ApiErrorCodeExamples(common = {ErrorCode.INVALID_TYPE_VALUE})
    @PostMapping("/log-processes/{logProcessId}/dedup-rules")
    public ApiResponse<DedupRuleResponse.CreateDTO> createDedupRule(
            @Parameter(description = "로그 프로세스 ID") @PathVariable Long logProcessId,
            @Valid @RequestBody DedupRuleRequest.CreateDTO dto) {
        return ApiResponse.onSuccess(SuccessCode.OK, dedupRuleFacade.createDedupRule(logProcessId, dto));
    }

    @Operation(summary = "중복제거 규칙 수정", description = "기존 중복제거 규칙의 내용을 수정합니다.")
    @ApiErrorCodeExamples(common = {ErrorCode.INVALID_TYPE_VALUE})
    @PatchMapping("/dedup-rules/{dedupRuleId}")
    public ApiResponse<DedupRuleResponse.UpdateDTO> updateDedupRule(
            @Parameter(description = "중복제거 규칙 ID") @PathVariable Long dedupRuleId,
            @Valid @RequestBody DedupRuleRequest.UpdateDTO dto) {
        return ApiResponse.onSuccess(SuccessCode.OK, dedupRuleFacade.updateDedupRule(dedupRuleId, dto));
    }

    @Operation(summary = "중복제거 규칙 삭제", description = "중복제거 규칙을 삭제(Soft Delete) 처리합니다.")
    @DeleteMapping("/dedup-rules/{dedupRuleId}")
    public ApiResponse<DedupRuleResponse.DeleteDTO> deleteDedupRule(
            @Parameter(description = "중복제거 규칙 ID") @PathVariable Long dedupRuleId) {
        return ApiResponse.onSuccess(SuccessCode.OK, dedupRuleFacade.deleteDedupRule(dedupRuleId));
    }

    @Operation(summary = "중복제거 규칙 목록 조회", description = "특정 로그 프로세스에 속한 중복제거 규칙 목록을 조회합니다.")
    @GetMapping("/log-processes/{logProcessId}/dedup-rules")
    public ApiResponse<PageResponse<DedupRuleResponse.ListDTO>> getDedupRules(
            @Parameter(description = "로그 프로세스 ID") @PathVariable Long logProcessId,
            @ParameterObject @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
                    Pageable pageable) {
        return ApiResponse.onSuccess(SuccessCode.OK, dedupRuleFacade.getDedupRules(logProcessId, pageable));
    }

    @Operation(summary = "중복제거 규칙 상세 조회", description = "특정 중복제거 규칙의 상세 정보를 조회합니다.")
    @GetMapping("/dedup-rules/{dedupRuleId}")
    public ApiResponse<DedupRuleResponse.DetailDTO> getDedupRule(
            @Parameter(description = "중복제거 규칙 ID") @PathVariable Long dedupRuleId) {
        return ApiResponse.onSuccess(SuccessCode.OK, dedupRuleFacade.getDedupRule(dedupRuleId));
    }
}
