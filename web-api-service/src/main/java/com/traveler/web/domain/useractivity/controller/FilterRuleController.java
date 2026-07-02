package com.traveler.web.domain.useractivity.controller;

import com.traveler.common.api.auth.annotation.RequireRole;
import com.traveler.common.core.code.ErrorCode;
import com.traveler.common.core.code.SuccessCode;
import com.traveler.common.core.response.ApiResponse;
import com.traveler.common.core.response.PageResponse;
import com.traveler.web.domain.useractivity.dto.request.FilterRuleRequest;
import com.traveler.web.domain.useractivity.dto.response.FilterRuleResponse;
import com.traveler.web.domain.useractivity.facade.FilterRuleFacade;
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

@Tag(name = "FilterRule", description = "FilterRule API")
@RestController
@RequiredArgsConstructor
@RequireRole("ROLE_ADMIN")
@RequestMapping("/api/v1/admin")
public class FilterRuleController {
    private final FilterRuleFacade filterRuleFacade;

    @Operation(summary = "필터 규칙 생성", description = "특정 로그 프로세스에 새로운 필터 규칙을 생성합니다.")
    @ApiErrorCodeExamples(common = {ErrorCode.INVALID_TYPE_VALUE})
    @PostMapping("/log-processes/{logProcessId}/filter-rules")
    public ApiResponse<FilterRuleResponse.CreateDTO> createFilterRule(
            @Parameter(description = "로그 프로세스 ID") @PathVariable Long logProcessId,
            @Valid @RequestBody FilterRuleRequest.CreateDTO dto) {
        return ApiResponse.onSuccess(SuccessCode.OK, filterRuleFacade.createFilterRule(logProcessId, dto));
    }

    @Operation(summary = "필터 규칙 수정", description = "기존 필터 규칙의 내용을 수정합니다.")
    @ApiErrorCodeExamples(common = {ErrorCode.INVALID_TYPE_VALUE})
    @PatchMapping("/filter-rules/{filterRuleId}")
    public ApiResponse<FilterRuleResponse.UpdateDTO> updateFilterRule(
            @Parameter(description = "필터 규칙 ID") @PathVariable Long filterRuleId,
            @Valid @RequestBody FilterRuleRequest.UpdateDTO dto) {
        return ApiResponse.onSuccess(SuccessCode.OK, filterRuleFacade.updateFilterRule(filterRuleId, dto));
    }

    @Operation(summary = "필터 규칙 삭제", description = "필터 규칙을 삭제(Soft Delete) 처리합니다.")
    @DeleteMapping("/filter-rules/{filterRuleId}")
    public ApiResponse<FilterRuleResponse.DeleteDTO> deleteFilterRule(
            @Parameter(description = "필터 규칙 ID") @PathVariable Long filterRuleId) {
        return ApiResponse.onSuccess(SuccessCode.OK, filterRuleFacade.deleteFilterRule(filterRuleId));
    }

    @Operation(summary = "필터 규칙 목록 조회", description = "특정 로그 프로세스에 속한 필터 규칙 목록을 조회합니다.")
    @GetMapping("/log-processes/{logProcessId}/filter-rules")
    public ApiResponse<PageResponse<FilterRuleResponse.ListDTO>> getFilterRules(
            @Parameter(description = "로그 프로세스 ID") @PathVariable Long logProcessId,
            @ParameterObject @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
                    Pageable pageable) {
        return ApiResponse.onSuccess(SuccessCode.OK, filterRuleFacade.getFilterRules(logProcessId, pageable));
    }

    @Operation(summary = "필터 규칙 상세 조회", description = "특정 필터 규칙의 상세 정보를 조회합니다.")
    @GetMapping("/filter-rules/{filterRuleId}")
    public ApiResponse<FilterRuleResponse.DetailDTO> getFilterRule(
            @Parameter(description = "필터 규칙 ID") @PathVariable Long filterRuleId) {
        return ApiResponse.onSuccess(SuccessCode.OK, filterRuleFacade.getFilterRule(filterRuleId));
    }
}
