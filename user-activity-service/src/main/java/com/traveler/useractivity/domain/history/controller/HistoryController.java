package com.traveler.useractivity.domain.history.controller;

import com.traveler.common.api.auth.annotation.RequireRole;
import com.traveler.common.core.code.SuccessCode;
import com.traveler.common.core.response.ApiResponse;
import com.traveler.common.core.response.PageResponse;
import com.traveler.useractivity.domain.history.dto.response.HistoryResponse;
import com.traveler.useractivity.domain.history.enums.FailStage;
import com.traveler.useractivity.domain.history.enums.HistoryStatus;
import com.traveler.useractivity.domain.history.service.HistoryQueryService;
import com.traveler.useractivity.global.exception.code.UserActivityServiceErrorCode;
import com.traveler.useractivity.global.swagger.ApiErrorCodeExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@Tag(name = "History", description = "History API")
@RestController
@RequiredArgsConstructor
@RequireRole("ROLE_ADMIN")
@RequestMapping("/v1/admin/histories")
public class HistoryController {
    private final HistoryQueryService historyQueryService;

    @Operation(summary = "처리 기록 목록 조회", description = "상태 및 실패 단계로 필터링하여 로그 처리 기록 목록을 조회합니다.")
    @GetMapping
    public ApiResponse<PageResponse<HistoryResponse.ListDTO>> getHistories(
            @Parameter(description = "처리 상태") @RequestParam(required = false) HistoryStatus status,
            @Parameter(description = "실패 단계") @RequestParam(required = false) FailStage stage,
            @ParameterObject @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
                    Pageable pageable) {
        return ApiResponse.onSuccess(SuccessCode.OK, historyQueryService.getHistories(status, stage, pageable));
    }

    @Operation(summary = "처리 기록 상세 조회", description = "특정 처리 기록의 상세 정보를 조회합니다.")
    @ApiErrorCodeExamples(UserActivityServiceErrorCode.HISTORY_NOT_FOUND)
    @GetMapping("/{historyId}")
    public ApiResponse<HistoryResponse.DetailDTO> getHistory(
            @Parameter(description = "처리 기록 ID") @PathVariable String historyId) {
        return ApiResponse.onSuccess(SuccessCode.OK, historyQueryService.getHistory(historyId));
    }
}
