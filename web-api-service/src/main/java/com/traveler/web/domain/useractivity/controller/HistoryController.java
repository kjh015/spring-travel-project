package com.traveler.web.domain.useractivity.controller;

import com.traveler.common.api.auth.annotation.RequireRole;
import com.traveler.common.core.code.SuccessCode;
import com.traveler.common.core.response.ApiResponse;
import com.traveler.common.core.response.PageResponse;
import com.traveler.web.domain.useractivity.dto.response.HistoryResponse;
import com.traveler.web.domain.useractivity.enums.FailStage;
import com.traveler.web.domain.useractivity.enums.HistoryStatus;
import com.traveler.web.domain.useractivity.facade.HistoryFacade;
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
@RequestMapping("/api/v1/admin/histories")
public class HistoryController {
    private final HistoryFacade historyFacade;

    @Operation(summary = "처리 기록 목록 조회", description = "상태 및 실패 단계로 필터링하여 로그 처리 기록 목록을 조회합니다.")
    @GetMapping
    public ApiResponse<PageResponse<HistoryResponse.ListDTO>> getHistories(
            @Parameter(description = "처리 상태") @RequestParam(required = false) HistoryStatus status,
            @Parameter(description = "실패 단계") @RequestParam(required = false) FailStage stage,
            @ParameterObject @PageableDefault(size = 10, sort = "timestamp", direction = Sort.Direction.DESC)
                    Pageable pageable) {
        return ApiResponse.onSuccess(SuccessCode.OK, historyFacade.getHistories(status, stage, pageable));
    }

    @Operation(summary = "처리 기록 상세 조회", description = "특정 처리 기록의 상세 정보를 조회합니다.")
    @GetMapping("/{historyId}")
    public ApiResponse<HistoryResponse.DetailDTO> getHistory(
            @Parameter(description = "처리 기록 ID") @PathVariable String historyId) {
        return ApiResponse.onSuccess(SuccessCode.OK, historyFacade.getHistory(historyId));
    }
}
